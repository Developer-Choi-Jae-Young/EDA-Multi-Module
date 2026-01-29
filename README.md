## 🌿 v6 - 동시성 문제 해결 <분산 락 (Distributed Lock feat. Redis)>

비관적 락(v5)은 단일 데이터베이스 환경에서는 강력하지만, 서버가 여러 대이거나 DB 인스턴스가 분리된 분산 환경에서는 데이터 정합성을 보장하기 어렵습니다. **v6**에서는 외부 인프라인 **Redis**를 사용하여 전역적인 락을 관리하는 시스템을 구축했습니다.

### ❓ 분산 락(Distributed Lock)이란?
여러 대의 서버(WAS)가 공통으로 사용하는 외부 저장소(Redis 등)를 활용하여, 어떤 서버에서 요청이 들어오더라도 동일한 자원에 대해서는 **전역적으로 단 하나의 락**만 존재하도록 보장하는 메커니즘입니다.



---

### ⚙️ 구동 원리 (Mechanism: Redisson 라이브러리 활용)

본 프로젝트에서는 Redis 클라이언트로 **Redisson**을 선택하여 효율적인 락을 구현했습니다.

1. **Pub/Sub 방식의 대기**: Redisson은 스핀 락(Spin Lock) 방식처럼 계속해서 락 획득을 시도(Polling)하여 Redis에 부하를 주지 않습니다. 대신, 락이 해제될 때까지 대기하다가 **해제 알림(Pub/Sub)**을 받으면 그때 획득을 시도합니다.
2. **타임아웃 설정 (Wait Time & Lease Time)**:
    - **Wait Time**: 락을 얻기 위해 무한정 대기하지 않고 지정된 시간 동안만 기다립니다.
    - **Lease Time**: 락을 획득한 후 일정 시간이 지나면 자동으로 해제하여, 특정 서버의 장애로 인해 락이 영구 점유되는 데드락 상황을 방지합니다.
3. **트랜잭션 시점 제어**: 락 획득과 해제는 반드시 트랜잭션의 시작과 종료보다 넓은 범위에서 이루어져야 데이터 정합성이 보장됩니다.

---

### 📊 분산 락의 장단점

| 구분 | 내용 |
| :--- | :--- |
| **장점** | - **분산 환경 보장**: 다중 서버 인스턴스 환경에서도 완벽하게 동시성을 제어합니다.<br>- **Redis 부하 감소**: Redisson의 Pub/Sub 메커니즘으로 네트워크 트래픽을 효율적으로 관리합니다.<br>- **안정성**: 타임아웃 설정을 통해 시스템 전체의 병목 현상을 유연하게 대처합니다. |
| **단점** | - **외부 의존성**: Redis 서버에 대한 추가적인 인프라 관리 및 네트워크 비용이 발생합니다.<br>- **복잡성**: 비즈니스 로직과 락 획득/해제 시점을 정확히 일치시켜야 정합성이 유지됩니다. |

---

### 🛠 구현 포인트: Redisson 락 적용 예시

```java
@EventListener
public void InventoryDecrement(InventoryDecrementEvent inventoryDecrementEvent) throws BuyException, ExistInventoryException {
    String lockKey = "inventory:lock:" + inventoryDecrementEvent.getProductId();

    // Redis 분산 락으로 동시성 제어
    redisLockService.executeWithLock(
            lockKey,
            10,  // 락 획득 대기 시간: 10초
            30,  // 락 유지 시간: 30초
            () -> {
                // 재고 조회
                InventoryEntity inventory = inventoryRepository.findByProduct(inventoryDecrementEvent.getProductId())
                        .orElseThrow(() -> new ExistInventoryException("재고 정보가 존재하지 않습니다.", 100));

                // 재고 부족 검증
                if(inventory.getQuantity() < inventoryDecrementEvent.getQuantity()) {
                    throw new BuyException("현재 남아있는 재고 보다 더 많은 주문으로 인한 예외 발생", 100);
                }

                // 재고 감소
                inventory.decrementInventory(inventoryDecrementEvent.getQuantity());
                inventoryDecrementEvent.setInventoryId(inventory.getId());

                return null;
            }
    );
}
```
---
## 🧪 동시성 테스트 및 검증 (Concurrency Test)

동시성 제어 로직이 실제로 작동하는지 확인하기 위해 `CountDownLatch`와 `ExecutorService`를 활용하여 멀티스레드 테스트를 수행했습니다.

### 🛠 테스트 주요 도구
* **`ExecutorService`**: 병렬 작업을 수행할 스레드 풀을 생성합니다. (예: 10개~100개의 스레드 생성)
* **`CountDownLatch`**: 모든 스레드가 작업을 완료할 때까지 메인 스레드를 대기시켜, 테스트 결과가 정확히 집계되도록 보장합니다.
* **`AtomicInteger`**: 멀티스레드 환경에서 안전하게 성공/실패 횟수를 카운트합니다.

### 📋 주요 테스트 시나리오

#### 1. 동시성 문제 재현 (Race Condition)
* **상황**: 10개의 스레드가 동시에 각각 2개씩 주문 시도 (총 20개 주문 시도).
* **기대 결과**: 재고가 10개뿐이므로, 동시성 제어가 없다면 재고가 음수가 되거나 판매된 수량과 남은 재고의 합이 초기 재고(10개)와 맞지 않음.
* **검증**: `assertEquals(초기재고, 판매량 + 남은재고)`를 통해 데이터의 원자성을 확인합니다.

#### 2. 재고 부족 상황의 부분 성공 테스트
* **상황**: 재고가 10개일 때, 10명이 동시에 3개씩 구매 시도 (총 30개 요청).
* **기대 결과**: 선착순으로 딱 3명만 성공하고, 나머지 7명은 반드시 실패해야 함.
* **검증**: `successCount <= 3` 조건을 통해 초과 판매 방지를 확인합니다.

#### 3. 극한의 동시성 테스트 (Extreme Test)
* **상황**: 100개의 요청을 동시에 발생시켜 시스템의 안정성 확인.
* **검증**: 어떤 상황에서도 **최종 재고는 0 이상**이어야 하며, 데이터 정합성이 깨지지 않아야 함.

---

### 🔍 테스트 코드 핵심 로직
```java
// 모든 스레드가 동시에 출발하도록 대기
CountDownLatch latch = new CountDownLatch(threadCount);

for (int i = 0; i < threadCount; i++) {
    executorService.submit(() -> {
        try {
            orderService.buy(dto); // 주문 시도
            successCount.incrementAndGet();
        } catch (Exception e) {
            failCount.incrementAndGet(); // 실패 집계
        } finally {
            latch.countDown(); // 완료 신호
        }
    });
}

latch.await(); // 모든 요청이 끝날 때까지 대기 후 결과 검증
```
---
### 🔍 Deep Dive: 스핀 락(Spin Lock) vs Redisson (Pub/Sub)

분산 락을 구현할 때 가장 먼저 마주하는 방식인 **스핀 락**과, 본 프로젝트에서 채택한 **Redisson**의 차이점을 설명합니다.

#### 1. 스핀 락 (Spin Lock)이란?
* **개념**: 락을 획득할 때까지 무한 루프를 돌며 "락이 해제되었나?"를 계속해서 확인하는 방식입니다.
* **동작**: 주로 Redis의 `SETNX` (Set if Not Exists) 명령어를 사용하여 구현합니다.
* **단점**:
    - **Redis 부하**: 락을 얻을 때까지 지속적으로 Redis에 요청을 보내므로 네트워크 및 CPU 자원을 과도하게 소모합니다.
    - **지연 시간**: 락 확인 주기(Sleep 타임)를 길게 잡으면 응답이 늦어지고, 짧게 잡으면 Redis 부하가 더 심해지는 트레이드오프가 발생합니다.



#### 2. Redisson의 Pub/Sub 방식 (본 프로젝트 적용)
* **개념**: 락이 해제될 때까지 무한 루프를 도는 것이 아니라, Redis의 **발행/구독(Pub/Sub)** 기능을 활용합니다.
* **동작**:
    1. 락 획득에 실패하면 해당 채널을 구독하고 대기 상태(Wait)로 들어갑니다.
    2. 락을 가졌던 다른 스레드가 작업을 마치고 락을 해제하며 "락이 해제됨" 메시지를 발행합니다.
    3. 알림을 받은 대기 스레드들만 다시 락 획득을 시도합니다.
* **장점**:
    - **효율성**: 무의미한 재시도(Polling)가 발생하지 않아 Redis 부하를 획기적으로 낮춥니다.
    - **안정성**: 대기 중인 스레드들이 알림을 받은 시점에만 움직이므로 시스템 리소스를 아낄 수 있습니다.

---

### 📊 비교 요약

| 구분 | 스핀 락 (Lettuce 등) | Redisson (Pub/Sub) |
| :--- | :--- | :--- |
| **재시도 방식** | 주기적으로 확인 (Polling) | 해제 알림 대기 (Subscribe) |
| **네트워크 부하** | 높음 (확인 횟수만큼 발생) | 낮음 (해제 시점에만 발생) |
| **구현 난이도** | 직접 구현 필요 | 라이브러리에서 기본 제공 |
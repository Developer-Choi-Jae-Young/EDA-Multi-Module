## 🌿 v5 - 동시성 문제 해결 <비관적 락 (Pessimistic Lock)>

낙관적 락은 충돌이 잦은 환경에서 수많은 재시도(Retry)를 발생시켜 시스템 부하를 가중시킵니다. **v5**에서는 DB의 트랜잭션 잠금 기법을 활용하여 데이터 수정 권한을 선점하는 **비관적 락**을 적용했습니다.

### ❓ 비관적 락(Pessimistic Lock)이란?
데이터 수정 시 충돌이 발생할 것이라고 "비관적"으로 가정하고, 데이터를 읽는 시점에 즉시 락을 걸어 다른 트랜잭션의 접근을 차단하는 방식입니다.



---

### ⚙️ 구동 원리 (Mechanism)

JPA의 `@Lock(LockModeType.PESSIMISTIC_WRITE)` 어노테이션을 사용하여 동작합니다.

1. **조회 및 잠금 (Lock Acquisition)**: 데이터를 조회할 때 DB 수준에서 `SELECT ... FOR UPDATE` 쿼리를 실행합니다.
2. **대기 (Waiting)**: 다른 트랜잭션이 해당 로우(Row)를 점유하고 있다면, 락이 해제될 때까지 이후의 요청들은 대기 상태에 머뭅니다.
3. **수정 및 해제**: 조회를 마친 트랜잭션이 수정을 완료하고 `Commit` 하는 순간 락이 자동으로 해제되며, 대기하던 다음 요청이 순차적으로 진입합니다.

---

### 📊 비관적 락의 장단점

| 구분 | 내용 |
| :--- | :--- |
| **장점** | - **데이터 정합성 보장**: 충돌이 빈번한 환경에서도 완벽한 정합성을 유지합니다.<br>- **재시도 로직 불필요**: 대기열이 DB 수준에서 관리되므로 `@Retry` 같은 별도 처리가 필요 없습니다.<br>- 데이터 수정이 확실시되는 상황에서 낙관적 락보다 성능이 우수할 수 있습니다. |
| **단점** | - **성능 저하(Blocking)**: 락을 잡고 있는 동안 다른 트랜잭션이 대기하므로 동시 처리량이 감소할 수 있습니다.<br>- **데드락(Deadlock) 위험**: 여러 자원을 서로 다른 순서로 점유할 경우 시스템이 멈출 수 있습니다. |

---

### 🛠 구현 포인트
* **Repository 설정**:
```java
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryEntity i where i.product = :productId")
    Optional<InventoryEntity> findByProductWithPessimisticLock(Long productId);
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
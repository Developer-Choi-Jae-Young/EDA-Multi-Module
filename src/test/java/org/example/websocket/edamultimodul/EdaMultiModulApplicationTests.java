package org.example.websocket.edamultimodul;

import org.example.websocket.inventory.entity.InventoryEntity;
import org.example.websocket.inventory.repository.InventoryRepository;
import org.example.websocket.order.dto.request.BuyOrderDto;
import org.example.websocket.order.service.OrderService;
import org.example.websocket.product.entity.ProductEntity;
import org.example.websocket.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class EdaMultiModulApplicationTests {
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    private Long productId;
    private Long inventoryId;

    @BeforeEach
    void setUp() {
        // 1. Product 생성
        ProductEntity product = ProductEntity.builder()
                .productName("테스트 상품")
                .description("동시성 테스트용")
                .price(10000)
                .build();
        ProductEntity savedProduct = productRepository.save(product);
        productId = savedProduct.getId();

        // 2. Inventory 생성 (재고 10개)
        InventoryEntity inventory = InventoryEntity.builder()
                .product(productId)
                .quantity(10)
                .build();
        InventoryEntity savedInventory = inventoryRepository.save(inventory);
        inventoryId = savedInventory.getId();
    }

    @Test
    @DisplayName("동시성 문제 재현 - 재고보다 많이 판매됨")
    void testRaceCondition() throws InterruptedException {
        // Given
        int threadCount = 10;  // 10개의 동시 요청
        int orderQuantity = 2; // 각각 2개씩 구매
        // 총 20개 주문 시도 (재고는 10개만 있음)

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // When - 10개 스레드가 동시에 주문
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    BuyOrderDto dto = new BuyOrderDto();
                    dto.setProductId(productId);
                    dto.setQuantity(orderQuantity);

                    orderService.buy(dto);
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("주문 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기
        executorService.shutdown();

        // Then - 결과 확인
        InventoryEntity finalInventory = inventoryRepository.findById(inventoryId).get();

        System.out.println("=== 테스트 결과 ===");
        System.out.println("성공한 주문: " + successCount.get());
        System.out.println("실패한 주문: " + failCount.get());
        System.out.println("최종 재고: " + finalInventory.getQuantity());
        System.out.println("예상 재고: " + (10 - successCount.get() * orderQuantity));

        // 검증
        int expectedInventory = 10 - (successCount.get() * orderQuantity);

        // ❌ 동시성 제어가 없으면 이 테스트는 실패할 것
        // 재고가 음수가 되거나, 예상과 다를 것
        assertTrue(finalInventory.getQuantity() >= 0,
                "재고는 음수가 될 수 없다");

        assertEquals(expectedInventory, finalInventory.getQuantity(),
                "재고는 정확히 계산되어야 한다");
    }

    @Test
    @DisplayName("재고 부족 시 일부만 성공해야 함")
    void testInsufficientStock() throws InterruptedException {
        // Given
        int threadCount = 10;  // 10개의 동시 요청
        int orderQuantity = 3; // 각각 3개씩 구매 시도
        // 총 30개 주문 시도 (재고는 10개)
        // → 최대 3명만 성공해야 함

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    BuyOrderDto dto = new BuyOrderDto();
                    dto.setProductId(productId);
                    dto.setQuantity(orderQuantity);

                    orderService.buy(dto);
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // Then
        InventoryEntity finalInventory = inventoryRepository.findById(inventoryId).get();

        System.out.println("=== 재고 부족 테스트 결과 ===");
        System.out.println("성공: " + successCount.get() + " (예상: 3명)");
        System.out.println("실패: " + failCount.get() + " (예상: 7명)");
        System.out.println("최종 재고: " + finalInventory.getQuantity());

        // 검증
        assertTrue(successCount.get() <= 3,
                "재고 10개로 3개씩 구매 시 최대 3명만 성공");

        assertTrue(failCount.get() >= 7,
                "나머지는 실패해야 함");

        assertTrue(finalInventory.getQuantity() >= 0,
                "재고는 0 이상이어야 함");
    }

    @Test
    @DisplayName("100개 동시 요청 - 극한 테스트")
    void testExtremeConcurrency() throws InterruptedException {
        // Given
        int threadCount = 100;
        int orderQuantity = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    BuyOrderDto dto = new BuyOrderDto();
                    dto.setProductId(productId);
                    dto.setQuantity(orderQuantity);

                    orderService.buy(dto);
                    successCount.incrementAndGet();

                } catch (Exception e) {
                    // 재고 부족 등의 정상적인 실패
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // Then
        InventoryEntity finalInventory = inventoryRepository.findById(inventoryId).get();

        System.out.println("=== 극한 동시성 테스트 ===");
        System.out.println("총 요청: " + threadCount);
        System.out.println("성공: " + successCount.get());
        System.out.println("최종 재고: " + finalInventory.getQuantity());

        // 핵심 검증
        assertEquals(10, successCount.get() + finalInventory.getQuantity(),
                "판매된 수량 + 남은 재고 = 초기 재고(10)");

        assertTrue(finalInventory.getQuantity() >= 0,
                "재고는 절대 음수가 될 수 없다");
    }

    @Test
    @DisplayName("동시성 문제 디버깅용 - 상세 로그")
    void testWithDetailedLog() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executorService.submit(() -> {
                try {
                    System.out.println("[Thread-" + threadNum + "] 시작");

                    // 재고 확인
                    InventoryEntity before = inventoryRepository.findById(inventoryId).get();
                    System.out.println("[Thread-" + threadNum + "] 주문 전 재고: " + before.getQuantity());

                    BuyOrderDto dto = new BuyOrderDto();
                    dto.setProductId(productId);
                    dto.setQuantity(2);

                    orderService.buy(dto);

                    InventoryEntity after = inventoryRepository.findById(inventoryId).get();
                    System.out.println("[Thread-" + threadNum + "] 주문 후 재고: " + after.getQuantity());
                    System.out.println("[Thread-" + threadNum + "] 성공!");

                } catch (Exception e) {
                    System.out.println("[Thread-" + threadNum + "] 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }
}

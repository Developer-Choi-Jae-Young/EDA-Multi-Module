package org.example.websocket.inventory.event.handler;

import lombok.RequiredArgsConstructor;
import org.example.websocket.common.exception.BuyException;
import org.example.websocket.common.exception.ExistInventoryException;
import org.example.websocket.common.event.InventoryDecrementEvent;
import org.example.websocket.common.lock.RedisLockService;
import org.example.websocket.inventory.entity.InventoryEntity;
import org.example.websocket.inventory.repository.InventoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventHandler {
    private final InventoryRepository inventoryRepository;
    private final RedisLockService redisLockService;

    /**
     * Redis 분산 락을 사용한 재고 감소
     */
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
}

package org.example.websocket.edamultimodul.event.handler;

import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.entity.InventoryEntity;
import org.example.websocket.edamultimodul.event.InventoryDecrementEvent;
import org.example.websocket.edamultimodul.exception.BuyException;
import org.example.websocket.edamultimodul.exception.ExistInventoryException;
import org.example.websocket.edamultimodul.repository.InventoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventHandler {
    private final InventoryRepository inventoryRepository;

    @EventListener
    public void InventoryDecrement(InventoryDecrementEvent inventoryDecrementEvent) throws BuyException, ExistInventoryException {
        InventoryEntity inventory = inventoryRepository.findByProduct(inventoryDecrementEvent.getProductId())
                .orElseThrow(() -> new ExistInventoryException("재고 정보가 존재하지 않습니다.", 100));

        if(inventory.getQuantity() < inventoryDecrementEvent.getQuantity()) {
            throw new BuyException("현재 남아있는 재고 보다 더 많은 주문으로 인한 예외 발생", 100);
        }

        inventory.decrementInventory(inventoryDecrementEvent.getQuantity());
        inventoryDecrementEvent.setInventoryId(inventory.getId());
    }
}

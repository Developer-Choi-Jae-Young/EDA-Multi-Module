package org.example.websocket.inventory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.common.exception.ExistProductException;
import org.example.websocket.common.event.ProductGetItemEvent;
import org.example.websocket.common.utils.ResultHolder;
import org.example.websocket.inventory.dto.request.InsertInventoryDto;
import org.example.websocket.inventory.entity.InventoryEntity;
import org.example.websocket.inventory.repository.InventoryRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<InventoryEntity> getInventoryList() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public InventoryEntity insertInventory(InsertInventoryDto insertInventoryDto) throws ExistProductException {
        ResultHolder<Long> productId = new ResultHolder<>();
        ProductGetItemEvent event = new ProductGetItemEvent(insertInventoryDto.getProductId() , productId::setValue);
        eventPublisher.publishEvent(event);

        InventoryEntity inventory = InventoryEntity.builder()
                                    .product(productId.getValue())
                                    .quantity(insertInventoryDto.getQuantity()).build();

        return inventoryRepository.save(inventory);
    }
}

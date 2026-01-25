package org.example.websocket.edamultimodul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.dto.request.InsertInventoryDto;
import org.example.websocket.edamultimodul.entity.InventoryEntity;
import org.example.websocket.edamultimodul.entity.ProductEntity;
import org.example.websocket.edamultimodul.event.ProductGetItemEvent;
import org.example.websocket.edamultimodul.exception.ExistProductException;
import org.example.websocket.edamultimodul.repository.InventoryRepository;
import org.example.websocket.edamultimodul.repository.ProductRepository;
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
        ProductGetItemEvent event = new ProductGetItemEvent(insertInventoryDto.getProductId());
        eventPublisher.publishEvent(event);

        InventoryEntity inventory = InventoryEntity.builder()
                                    .product(event.getProductId())
                                    .quantity(insertInventoryDto.getQuantity()).build();

        return inventoryRepository.save(inventory);
    }
}

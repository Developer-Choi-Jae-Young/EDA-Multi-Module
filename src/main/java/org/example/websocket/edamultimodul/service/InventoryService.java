package org.example.websocket.edamultimodul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.dto.request.InsertInventoryDto;
import org.example.websocket.edamultimodul.entity.InventoryEntity;
import org.example.websocket.edamultimodul.entity.ProductEntity;
import org.example.websocket.edamultimodul.exception.ExistProductException;
import org.example.websocket.edamultimodul.repository.InventoryRepository;
import org.example.websocket.edamultimodul.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public List<InventoryEntity> getInventoryList() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public InventoryEntity insertInventory(InsertInventoryDto insertInventoryDto) throws ExistProductException {
        ProductEntity product = productRepository.findById(insertInventoryDto.getProductId()).orElseThrow(() -> new ExistProductException("상품이 존재하지 않습니다.", 100));
        InventoryEntity inventory = InventoryEntity.builder()
                                    .product(product)
                                    .quantity(insertInventoryDto.getQuantity()).build();
        return inventoryRepository.save(inventory);
    }
}

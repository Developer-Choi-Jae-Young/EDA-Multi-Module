package org.example.websocket.inventory.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.inventory.entity.InventoryEntity;

@Data
@Builder
public class InsertInventoryDto {
    private Long id;
    private Long product;
    private Integer quantity;

    public static InsertInventoryDto of (InventoryEntity inventoryEntity) {
        return InsertInventoryDto.builder()
                .id(inventoryEntity.getId())
                .product(inventoryEntity.getProduct())
                .quantity(inventoryEntity.getQuantity()).build();
    }
}

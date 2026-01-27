package org.example.websocket.inventory.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.inventory.entity.InventoryEntity;

@Data
@Builder
public class InventoryListDto {
    private Long id;
    private Long product;
    private Integer quantity;

    public static InventoryListDto of (InventoryEntity inventoryEntity) {
        return InventoryListDto.builder()
                .id(inventoryEntity.getId())
                .product(inventoryEntity.getProduct())
                .quantity(inventoryEntity.getQuantity()).build();
    }
}

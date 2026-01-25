package org.example.websocket.edamultimodul.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.edamultimodul.entity.InventoryEntity;
import org.example.websocket.edamultimodul.entity.MemberEntity;

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

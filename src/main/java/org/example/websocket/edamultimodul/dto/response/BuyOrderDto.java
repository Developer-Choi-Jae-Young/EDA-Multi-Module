package org.example.websocket.edamultimodul.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.edamultimodul.entity.OrderEntity;

@Data
@Builder
public class BuyOrderDto {
    private ProductListDto product;
    private Integer quantity;
    private InventoryListDto inventory;

    public static BuyOrderDto of(OrderEntity orderEntity) {
        return BuyOrderDto.builder()
                .product(ProductListDto.of(orderEntity.getProduct()))
                .quantity(orderEntity.getQuantity())
                .inventory(InventoryListDto.of(orderEntity.getInventory()))
                .build();
    }
}

package org.example.websocket.edamultimodul.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.edamultimodul.entity.OrderEntity;

@Data
@Builder
public class BuyOrderDto {
    private Long product;
    private Integer quantity;
    private Long inventory;

    public static BuyOrderDto of(OrderEntity orderEntity) {
        return BuyOrderDto.builder()
                .product(orderEntity.getProduct())
                .inventory(orderEntity.getInventory())
                .quantity(orderEntity.getQuantity())
                .build();
    }
}

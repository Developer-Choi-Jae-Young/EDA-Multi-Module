package org.example.websocket.order.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.order.entity.OrderEntity;

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

package org.example.websocket.order.dto.request;

import lombok.Data;

@Data
public class BuyOrderDto {
    private Long productId;
    private Integer quantity;
}

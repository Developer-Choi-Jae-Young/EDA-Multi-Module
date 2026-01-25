package org.example.websocket.edamultimodul.dto.request;

import lombok.Data;

@Data
public class BuyOrderDto {
    private Long productId;
    private Integer quantity;
}

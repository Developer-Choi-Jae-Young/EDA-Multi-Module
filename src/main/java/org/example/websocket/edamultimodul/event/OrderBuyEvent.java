package org.example.websocket.edamultimodul.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderBuyEvent {
    private Long productId;
    private Integer quantity;
}

package org.example.websocket.edamultimodul.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductGetItemEvent {
    private Long productId;
}

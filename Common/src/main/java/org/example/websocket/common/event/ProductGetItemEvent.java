package org.example.websocket.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductGetItemEvent {
    private Long productId;
}

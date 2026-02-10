package org.example.websocket.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
public class ProductGetItemEvent {
    private Long productId;
    private Consumer<Long> consumer;
}

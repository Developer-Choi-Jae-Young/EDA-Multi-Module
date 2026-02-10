package org.example.websocket.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
public class InventoryDecrementEvent {
    private Integer quantity;
    private Long productId;
    private Consumer<Long> consumer;
}

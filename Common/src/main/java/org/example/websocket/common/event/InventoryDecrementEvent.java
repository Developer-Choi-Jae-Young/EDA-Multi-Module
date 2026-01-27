package org.example.websocket.common.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDecrementEvent {
    private Long productId;
    private Integer quantity;
    private Long inventoryId;

    public InventoryDecrementEvent(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}

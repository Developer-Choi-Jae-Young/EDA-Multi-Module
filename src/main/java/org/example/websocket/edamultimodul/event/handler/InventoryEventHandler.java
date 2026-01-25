package org.example.websocket.edamultimodul.event.handler;

import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.event.InventoryDecrementEvent;
import org.example.websocket.edamultimodul.repository.InventoryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class InventoryEventHandler {
    private final InventoryRepository inventoryRepository;

    @TransactionalEventListener
    public void OrderSell(InventoryDecrementEvent inventoryDecrementEvent) {

    }
}

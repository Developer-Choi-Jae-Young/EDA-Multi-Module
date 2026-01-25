package org.example.websocket.edamultimodul.event.handler;

import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.event.OrderBuyEvent;
import org.example.websocket.edamultimodul.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {
    private final OrderRepository orderRepository;

    @TransactionalEventListener
    public void OrderBuy(OrderBuyEvent orderBuyEvent) {

    }
}

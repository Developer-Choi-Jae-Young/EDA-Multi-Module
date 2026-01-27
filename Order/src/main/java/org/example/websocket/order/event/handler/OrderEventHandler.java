package org.example.websocket.order.event.handler;

import lombok.RequiredArgsConstructor;
import org.example.websocket.common.event.OrderBuyEvent;
import org.example.websocket.order.repository.OrderRepository;
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

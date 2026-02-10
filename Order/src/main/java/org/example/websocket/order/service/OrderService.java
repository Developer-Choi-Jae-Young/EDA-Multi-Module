package org.example.websocket.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.common.event.InventoryDecrementEvent;
import org.example.websocket.common.utils.ResultHolder;
import org.example.websocket.order.dto.request.BuyOrderDto;
import org.example.websocket.order.entity.OrderEntity;
import org.example.websocket.order.entity.enums.OrderType;
import org.example.websocket.order.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderEntity buy(BuyOrderDto buyOrderDto) {
        ResultHolder<Long> inventoryId = new ResultHolder<>();
        InventoryDecrementEvent inventoryEvent = new InventoryDecrementEvent(buyOrderDto.getQuantity(), buyOrderDto.getProductId(), inventoryId::setValue);
        eventPublisher.publishEvent(inventoryEvent);

        OrderEntity orderEntity = OrderEntity.builder()
                .orderType(OrderType.DECREMENT)
                .quantity(buyOrderDto.getQuantity())
                .inventory(inventoryId.getValue())
                .product(buyOrderDto.getProductId())
                .build();

        return orderRepository.save(orderEntity);
    }
}

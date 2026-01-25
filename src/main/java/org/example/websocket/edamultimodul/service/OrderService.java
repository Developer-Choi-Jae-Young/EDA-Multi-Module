package org.example.websocket.edamultimodul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.dto.request.BuyOrderDto;
import org.example.websocket.edamultimodul.entity.OrderEntity;
import org.example.websocket.edamultimodul.entity.enums.OrderType;
import org.example.websocket.edamultimodul.event.InventoryDecrementEvent;
import org.example.websocket.edamultimodul.repository.OrderRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public OrderEntity buy(BuyOrderDto buyOrderDto) {
        InventoryDecrementEvent inventoryEvent = new InventoryDecrementEvent(buyOrderDto.getProductId(), buyOrderDto.getQuantity());
        eventPublisher.publishEvent(inventoryEvent);

        OrderEntity orderEntity = OrderEntity.builder()
                .orderType(OrderType.DECREMENT)
                .quantity(buyOrderDto.getQuantity())
                .inventory(inventoryEvent.getInventoryId())
                .product(inventoryEvent.getProductId())
                .build();

        return orderRepository.save(orderEntity);
    }
}

package org.example.websocket.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.common.event.InventoryDecrementEvent;
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

    /**
     * Order 모듈의 트랜잭션 경계 문제
     *
     * 시나리오 1: Order 저장 실패
     *
     * 1. Inventory 감소 성공 (재고 10 → 7)
     * 2. Order 저장 시도
     * 3. Order 저장 실패 (DB 제약 조건, 네트워크 등)
     * 4. Order 모듈의 @Transactional 롤백
     * 5. 하지만 Inventory의 감소는 이미 커밋됨 ❌
     *
     * 결과: 재고만 감소하고 주문은 없는 상태 (데이터 정합성 깨짐)
     */
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

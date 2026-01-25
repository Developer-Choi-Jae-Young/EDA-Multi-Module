package org.example.websocket.edamultimodul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.dto.request.BuyOrderDto;
import org.example.websocket.edamultimodul.entity.InventoryEntity;
import org.example.websocket.edamultimodul.entity.OrderEntity;
import org.example.websocket.edamultimodul.entity.ProductEntity;
import org.example.websocket.edamultimodul.entity.enums.OrderType;
import org.example.websocket.edamultimodul.exception.BuyException;
import org.example.websocket.edamultimodul.exception.ExistProductException;
import org.example.websocket.edamultimodul.repository.InventoryRepository;
import org.example.websocket.edamultimodul.repository.OrderRepository;
import org.example.websocket.edamultimodul.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public OrderEntity buy(BuyOrderDto buyOrderDto) throws ExistProductException, BuyException {
        ProductEntity product = productRepository.findById(buyOrderDto.getProductId()).orElseThrow(() -> new ExistProductException("상품이 존재하지 않습니다.", 100));
        InventoryEntity inventory = inventoryRepository.findByProduct(product);

        if(inventory.getQuantity() < buyOrderDto.getQuantity()) {
            throw new BuyException("현재 남아있는 재고 보다 더 많은 주문으로 인한 예외 발생", 100);
        }

        inventory.decrementInventory(buyOrderDto.getQuantity());

        OrderEntity orderEntity = OrderEntity.builder()
                .orderType(OrderType.DECREMENT)
                .quantity(buyOrderDto.getQuantity())
                .product(product)
                .inventory(inventory)
                .build();

        return orderRepository.save(orderEntity);
    }
}

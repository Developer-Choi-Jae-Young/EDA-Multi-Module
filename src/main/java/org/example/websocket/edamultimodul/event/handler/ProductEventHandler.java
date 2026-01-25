package org.example.websocket.edamultimodul.event.handler;

import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.entity.ProductEntity;
import org.example.websocket.edamultimodul.event.ProductGetItemEvent;
import org.example.websocket.edamultimodul.exception.ExistProductException;
import org.example.websocket.edamultimodul.repository.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEventHandler {
    private final ProductRepository productRepository;

    @EventListener
    public void getProductItem(ProductGetItemEvent productGetItemEvent) throws ExistProductException {
        ProductEntity product = productRepository.findById(productGetItemEvent.getProductId()).orElseThrow(() -> new ExistProductException("상품이 존재하지 않습니다.", 100));
        productGetItemEvent.setProductId(product.getId());
    }
}

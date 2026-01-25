package org.example.websocket.edamultimodul.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.entity.ProductEntity;
import org.example.websocket.edamultimodul.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public ProductEntity insertProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public List<ProductEntity> getProductList() {
        return productRepository.findAll();
    }
}

package org.example.websocket.edamultimodul.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.edamultimodul.entity.ProductEntity;

@Data
@Builder
public class ProductListDto {
    private Long id;
    private String productName;
    private String description;
    private Integer price;

    public static ProductListDto of(ProductEntity productEntity) {
        return ProductListDto.builder()
                .id(productEntity.getId())
                .productName(productEntity.getProductName())
                .description(productEntity.getDescription())
                .price(productEntity.getPrice())
                .build();
    }
}

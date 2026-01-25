package org.example.websocket.edamultimodul.dto.request;

import lombok.Data;
import org.example.websocket.edamultimodul.entity.ProductEntity;

@Data
public class InsertProductDto {
    private String productName;
    private String description;
    private Integer price;

    public static ProductEntity of(InsertProductDto insertProductDto) {
        return ProductEntity.builder()
                .productName(insertProductDto.getProductName())
                .description(insertProductDto.getDescription())
                .price(insertProductDto.getPrice()).build();
    }
}

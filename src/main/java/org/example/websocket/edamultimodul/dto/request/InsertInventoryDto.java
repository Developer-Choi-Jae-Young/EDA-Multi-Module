package org.example.websocket.edamultimodul.dto.request;

import lombok.Data;

@Data
public class InsertInventoryDto {
    private Long productId;
    private Integer quantity;
}

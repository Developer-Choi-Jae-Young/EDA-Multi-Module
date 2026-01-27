package org.example.websocket.inventory.dto.request;

import lombok.Data;

@Data
public class InsertInventoryDto {
    private Long productId;
    private Integer quantity;
}

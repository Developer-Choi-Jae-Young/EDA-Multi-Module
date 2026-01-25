package org.example.websocket.edamultimodul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.websocket.edamultimodul.entity.enums.OrderType;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity product;
    @ManyToOne
    @JoinColumn(name = "inventoryId")
    private InventoryEntity inventory;
}

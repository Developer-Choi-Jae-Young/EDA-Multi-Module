package org.example.websocket.edamultimodul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "INVENTORY")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity product;

    public void decrementInventory(int quantity) {
        this.quantity-= quantity;
    }
}

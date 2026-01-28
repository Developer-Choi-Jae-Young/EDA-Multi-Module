package org.example.websocket.inventory.entity;

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
    @Column(name = "productId")
    private Long product;

//    @Version  // <- 버전 컬럼 추가
//    private Long version;

    public void decrementInventory(int quantity) {
        this.quantity-= quantity;
    }
}

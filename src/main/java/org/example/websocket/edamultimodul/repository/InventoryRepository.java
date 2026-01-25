package org.example.websocket.edamultimodul.repository;

import org.example.websocket.edamultimodul.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {
    Optional<InventoryEntity> findByProduct(Long productId);
}

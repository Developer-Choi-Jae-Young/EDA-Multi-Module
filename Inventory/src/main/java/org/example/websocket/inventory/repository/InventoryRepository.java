package org.example.websocket.inventory.repository;

import jakarta.persistence.LockModeType;
import org.example.websocket.inventory.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // <- 비관적 쓰기 락
    Optional<InventoryEntity> findByProduct(Long productId);
}

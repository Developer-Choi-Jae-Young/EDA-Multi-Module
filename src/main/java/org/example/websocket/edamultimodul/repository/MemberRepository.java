package org.example.websocket.edamultimodul.repository;

import org.example.websocket.edamultimodul.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Long countByEmail(String email);
}

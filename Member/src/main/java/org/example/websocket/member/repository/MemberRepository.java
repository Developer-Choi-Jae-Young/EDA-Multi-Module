package org.example.websocket.member.repository;

import org.example.websocket.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Long countByEmail(String email);
}

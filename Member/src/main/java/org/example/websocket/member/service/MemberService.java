package org.example.websocket.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.websocket.common.exception.DuplicationMemberException;
import org.example.websocket.member.entity.MemberEntity;
import org.example.websocket.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberEntity insertMember(MemberEntity memberEntity) throws DuplicationMemberException {
        if(!duplicateMemeber(memberEntity.getEmail())) {
            throw new DuplicationMemberException("이미 가입된 사용자가 존재합니다.", 100);
        }
        return memberRepository.save(memberEntity);
    }

    public List<MemberEntity> getMemberList() {
        return memberRepository.findAll();
    }

    private boolean duplicateMemeber(String email) {
        return memberRepository.countByEmail(email) <= 0;
    }
}

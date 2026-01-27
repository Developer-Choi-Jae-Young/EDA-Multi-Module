package org.example.websocket.member.dto.request;

import lombok.Data;
import org.example.websocket.member.entity.MemberEntity;

@Data
public class InsertMemberDto {
    private String userName;
    private String email;
    private String password;

    public static MemberEntity of(InsertMemberDto insertMemberDto) {
        return MemberEntity.builder().
                userName(insertMemberDto.getUserName()).
                email(insertMemberDto.getEmail()).
                password(insertMemberDto.getPassword()).build();
    }
}

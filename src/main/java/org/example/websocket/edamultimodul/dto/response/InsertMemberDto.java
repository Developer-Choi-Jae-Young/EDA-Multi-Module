package org.example.websocket.edamultimodul.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.edamultimodul.entity.MemberEntity;

@Data
@Builder
public class InsertMemberDto {
    private Long id;
    private String email;
    private String userName;

    public static InsertMemberDto of (MemberEntity memberEntityr) {
        return InsertMemberDto.builder()
                .id(memberEntityr.getId())
                .email(memberEntityr.getEmail())
                .userName(memberEntityr.getUserName()).build();
    }
}

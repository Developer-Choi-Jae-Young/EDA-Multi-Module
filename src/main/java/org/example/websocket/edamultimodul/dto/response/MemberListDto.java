package org.example.websocket.edamultimodul.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.websocket.edamultimodul.entity.MemberEntity;

@Data
@Builder
public class MemberListDto {
    private Long id;
    private String email;
    private String userName;

    public static MemberListDto of(MemberEntity memberEntity) {
        return MemberListDto.builder()
                .id(memberEntity.getId())
                .email(memberEntity.getEmail())
                .userName(memberEntity.getUserName())
                .build();
    }
}

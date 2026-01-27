package org.example.websocket.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.websocket.common.exception.DuplicationMemberException;
import org.example.websocket.member.dto.request.InsertMemberDto;
import org.example.websocket.member.dto.response.MemberListDto;
import org.example.websocket.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/insert")
    public ResponseEntity<?> insertMember(@RequestBody InsertMemberDto insertMemberDto) throws DuplicationMemberException {
        org.example.websocket.member.dto.response.InsertMemberDto response =
                org.example.websocket.member.dto.response.InsertMemberDto.of(memberService.insertMember(InsertMemberDto.of(insertMemberDto)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getMemberList() {
        List<MemberListDto> response = memberService.getMemberList().stream().map(MemberListDto::of).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

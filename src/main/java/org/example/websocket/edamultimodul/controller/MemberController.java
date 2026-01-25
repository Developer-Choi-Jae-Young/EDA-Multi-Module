package org.example.websocket.edamultimodul.controller;

import lombok.RequiredArgsConstructor;
import org.example.websocket.edamultimodul.dto.request.InsertMemberDto;
import org.example.websocket.edamultimodul.dto.response.MemberListDto;
import org.example.websocket.edamultimodul.exception.DuplicationMemberException;
import org.example.websocket.edamultimodul.service.MemberService;
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
        org.example.websocket.edamultimodul.dto.response.InsertMemberDto response =
                org.example.websocket.edamultimodul.dto.response.InsertMemberDto.of(memberService.insertMember(InsertMemberDto.of(insertMemberDto)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getMemberList() {
        List<MemberListDto> response = memberService.getMemberList().stream().map(MemberListDto::of).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.example.talktudy.controller.member;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
@ApiOperation("회원 Member Rest API 컨트롤러")
public class MemberController {
    private final MemberService memberService;

    @ApiOperation("회원 조회 api - 헤더의 토큰을 받아 회원 정보 조회")
    @GetMapping()
    public ResponseEntity<MemberDTO> getMember(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(memberService.getMember(customUserDetails.getMemberId()));
    }

    @ApiOperation("회원 수정 api - 헤더의 토큰과 정보를 받고, 회원 정보 수정하기")
    @PutMapping()
    public ResponseEntity<MemberDTO> updateMember(@AuthenticationPrincipal CustomUserDetails customUserDetails, MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.updateMember(customUserDetails.getMemberId(), memberDTO));
    }

} // end class

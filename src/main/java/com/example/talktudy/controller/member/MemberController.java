package com.example.talktudy.controller.member;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.dto.auth.PasswordRequest;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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

    @ApiOperation("회원 수정 api - 헤더의 토큰과 정보를 받고, 회원 정보 수정.")
    @PutMapping()
    public ResponseEntity<MemberDTO> updateMember(@AuthenticationPrincipal CustomUserDetails customUserDetails, MemberDTO memberDTO) {
        return ResponseEntity.ok(memberService.updateMember(customUserDetails.getMemberId(), memberDTO));
    }

    @ApiOperation("비밀번호 변경 api - 헤더의 토큰을 받고, 패스워드를 변경한다.")
    @ApiResponse(code = 400, message = "기존 비밀번호가 일치하지 않았을때, 비밀번호를 입력하지 않았을때 Bad Request로 리턴됩니다.")
    @PostMapping("/password")
    public ResponseEntity<ResponseDTO> changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PasswordRequest passwordRequest) {
        return ResponseEntity.ok(memberService.changePassword(customUserDetails.getMemberId(), passwordRequest.getOldPassword(), passwordRequest.getNewPassword()));
    }

} // end class

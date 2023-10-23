package com.example.talktudy.controller.chat;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.dto.chat.ChatRoomDTO;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.chat.ChatService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat")
@Api(tags = {"ChatRoom API - 채팅방 관련 Api"})
public class ChatRoomController {
    private final ChatService chatService;

    @ApiOperation(value = "채팅방 리스트 조회 api - 헤더의 토큰을 받고, 사용자가 참여한 채팅방 리스트 조회")
    @GetMapping
    public ResponseEntity<List<ChatRoomDTO>> getChatRooms(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(chatService.getChatRooms(customUserDetails.getMemberId()));
    }

    @ApiOperation(value = "채팅방 단일 조회 api - 채팅룸 아이디를 받아 채팅방의 메세지 목록과 정보를 조회한다.")
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDTO> getChatRoomAndMessage(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatService.getChatRoom(chatRoomId));
    }

    @ApiOperation(value = "스터디 지원 채팅방 단일 조회 api - 스터디 아이디를 받아 스터디 지원 채팅방의 메세지 목록과 정보를 조회한다.")
    @GetMapping("/study/apply/{studyId}")
    public ResponseEntity<ChatRoomDTO> getStudyApplyChatRoom(@PathVariable Long studyId) {
        return ResponseEntity.ok(chatService.getStudyApplyChatRoom(studyId));
    }

    @ApiOperation("채팅방 제목 변경 api - 헤더의 토큰을 받아, 회원이 개설자면 채팅방의 타이틀을 변경")
    @PutMapping("/{chatRoomId}")
    public ResponseEntity<ResponseDTO> changeChatRoomTitle(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long chatRoomId,
            @RequestParam(value = "title", required = true) String title) {
        return ResponseEntity.ok(chatService.changeChatRoomTitle(customUserDetails.getMemberId(), chatRoomId, title));
    }


//    @ApiOperation(value = "채팅방 입장 인원 api - 헤더의 토큰을 받고, 채팅방에 입장(스터디, 스터디 신청, 팀채팅)시 채팅방 멤버 관리 로직", notes = "채팅방에 인원을 체크하고, 최대 인원수에 도달하지 않았으면 입장을 허용하며 현재 인원수가 증가. Stomp Connection 이전에 호출 필수.")
//    @PostMapping("/enter")
//    public ResponseEntity<ChatRoomDTO> enterChatRoom(
//            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
//
//            @ApiParam(value = "채팅팀 채팅방이면 teamId(1)")
//            @RequestParam(value = "teamId", required = false) Long teamId,
//
//            @ApiParam(value = "스터디 채팅방이면 studyId(1)")
//            @RequestParam(value = "studyId", required = false) Long studyId,
//
//            @ApiParam(value = "스터디 지원, 신청 QNA 채팅방이면 true와 함께 studyId 필요, 그 외는 X")
//            @RequestParam(value = "studyApply", required = false) String studyApplyId
//            ) {
//        return ResponseEntity.ok(chatService.enterChatRoom(customUserDetails.getMemberId(), teamId, studyId, studyApplyId));
//    }

} // end class

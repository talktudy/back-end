package com.example.talktudy.controller.chat;

import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.dto.chat.ChatRoomDTO;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.chat.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat")
@Api(tags = {"ChatRoom API - 채팅방 관련 Api"})
public class ChatRoomController {
    private final ChatService chatService;

    @ApiOperation(value = "채팅방 입장 인원 api - 헤더의 토큰을 받고, 채팅방에 입장(스터디, 스터디 신청, 팀채팅)시 채팅방 멤버 관리 로직", notes = "채팅방에 인원을 체크하고, 최대 인원수에 도달하지 않았으면 입장을 허용하며 현재 인원수가 증가. Stomp Connection 이전에 호출 필수.")
    @PostMapping("/enter")
    public ResponseEntity<ChatRoomDTO> enterChatRoom(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,

            @ApiParam(value = "채팅팀 채팅방이면 teamId(1)")
            @RequestParam(value = "teamId", required = false) Long teamId,

            @ApiParam(value = "스터디 채팅방이면 studyId(1)")
            @RequestParam(value = "studyId", required = false) Long studyId,

            @ApiParam(value = "스터디 지원, 신청 QNA 채팅방이면 true와 함께 studyId 필요, 그 외는 X")
            @RequestParam(value = "studyApply", required = false) String studyApplyId
            ) {
        return ResponseEntity.ok(chatService.enterChatRoom(customUserDetails.getMemberId(), teamId, studyId, studyApplyId));
    }


    // 특정 채팅방 조회
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//        return chatService.findById(roomId);
//    }

} // end class

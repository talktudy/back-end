package com.example.talktudy.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    public enum MessageType { ENTER, TALK, LEAVE }

    @ApiModelProperty(name = "chatMessageId", value = "메세지 아이디", example = "1")
    private Long chatMessageId;

    @ApiModelProperty(name = "messageType", value = "채팅 메세지의 타입 : ENTER(입장시), TALK(대화), LEAVE(퇴장시)", example = "ENTER")
    private MessageType messageType;

    @ApiModelProperty(name = "chatRoomId", value = "채팅방 아이디", example = "1")
    private Long chatRoomId;

    @ApiModelProperty(name = "email", value = "sender의 회원 이메일", example = "test@test.com")
    private String email;

    @ApiModelProperty(name = "nickname", value = "sender의 회원 닉네임", example = "테스트인닉네임")
    private String nickname;

    @ApiModelProperty(name = "message", value = "채팅 메세지", example = "안녕하세요, 만나서 반갑습니다.")
    private String message;

    @ApiModelProperty(name = "profileImageUrl", value = "sender의 회원 이미지 추가", example = "http://image.com")
    private String profileImageUrl;

    @ApiModelProperty(name = "sentDate", value = "채팅 메세지가 생성된 날짜", example = "2023-10-22T12:12:1121")
    private LocalDateTime sentDate;
} // end class


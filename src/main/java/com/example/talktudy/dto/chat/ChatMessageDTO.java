package com.example.talktudy.dto.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChatMessageDTO {
    public enum MessageType { ENTER, TALK, LEAVE }

    @ApiModelProperty(name = "messageType", value = "채팅 메세지의 타입 : ENTER(입장시), TALK(대화), LEAVE(퇴장시)", example = "ENTER")
    private MessageType messageType;

    @ApiModelProperty(name = "chatRoomId", value = "채팅방 아이디", example = "1")
    private Long chatRoomId;

    @ApiModelProperty(name = "nickname", value = "sender의 회원 닉네임", example = "테스트인닉네임")
    private String nickname;

    @ApiModelProperty(name = "message", value = "채팅 메세지", example = "안녕하세요, 만나서 반갑습니다.")
    private String message;
} // end class


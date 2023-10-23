package com.example.talktudy.controller.chat;

import com.example.talktudy.dto.chat.ChatMessageDTO;
import com.example.talktudy.service.chat.ChatMessageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/api/chat/message")
    public void sendMessage(ChatMessageDTO chatMessage) {
        if (ChatMessageDTO.MessageType.ENTER.equals(chatMessage.getMessageType())) {
            chatMessage.setMessage(chatMessage.getNickname() + "님이 입장하였습니다.");
        } else if (ChatMessageDTO.MessageType.LEAVE.equals(chatMessage.getMessageType())) {
            chatMessage.setMessage(chatMessage.getNickname() + "님이 퇴장하셨습니다.");
        }

        chatMessageService.saveMessage(chatMessage);

        sendingOperations.convertAndSend("/topic/api/chat/room/" + chatMessage.getChatRoomId(), chatMessage);
    }

} // End class

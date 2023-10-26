package com.example.talktudy.service.chat;

import com.example.talktudy.dto.chat.ChatMessageDTO;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.chat.ChatMessage;
import com.example.talktudy.repository.chat.ChatMessageRepository;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.repository.chat.ChatRoomRepository;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatMessageDTO saveMessage(ChatMessageDTO chatMessage) {
        // 1. DB에서 회원을 찾는다.
        Member member = memberRepository.findByEmail(chatMessage.getEmail()).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. ChatRoom을 찾는다.
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getChatRoomId()).orElseThrow(() -> new CustomNotFoundException("채팅방 정보를 찾을 수 없습니다."));

        // 3. ChatMessage 객체 생성
        ChatMessage chat = ChatMessage.builder()
                .chatRoom(chatRoom)
                .member(member)
                .message(chatMessage.getMessage())
                .sentDate(LocalDateTime.now())
                .build();

        // 4. 저장
        ChatMessage newChatMessage = chatMessageRepository.save(chat);

        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setChatMessageId(newChatMessage.getChatMessageId());
        chatMessageDTO.setMessageType(chatMessage.getMessageType());
        chatMessageDTO.setChatRoomId(newChatMessage.getChatRoom().getChatRoomId());
        chatMessageDTO.setEmail(newChatMessage.getMember().getEmail());
        chatMessageDTO.setNickname(newChatMessage.getMember().getNickname());
        chatMessageDTO.setMessage(newChatMessage.getMessage());
        chatMessageDTO.setProfileImageUrl(newChatMessage.getMember().getProfileImageUrl());
        chatMessageDTO.setSentDate(newChatMessage.getSentDate());

        return chatMessageDTO;
    }
} // end class

package com.example.talktudy.service.chat;

import com.example.talktudy.dto.chat.ChatRoomDTO;
import com.example.talktudy.repository.chat.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatRoomMapper {
    ChatRoomMapper INSTANCE = Mappers.getMapper(ChatRoomMapper.class);

    ChatRoomDTO chatRoomEntityToDto(ChatRoom chatRoom);
}

package com.example.talktudy.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatRoomDTO {
    // chatRoomId, name, studyId, teamId, isStudyApply
    @ApiModelProperty(name = "chatRoomId", value = "채팅방 아이디", example = "1")
    private Long chatRoomId;

    @ApiModelProperty(name = "name", value = "채팅방 이름", example = "백엔드 스터디 채팅방")
    private String name;

    @ApiModelProperty(name = "studyId", value = "스터디 아이디", example = "1")
    private Long studyId;

    @ApiModelProperty(name = "teamId", value = "팀 아이디", example = "1")
    private Long teamId;

    @ApiModelProperty(name = "isStudyApply", value = "스터디 신청 채팅방의 여부", example = "true")
    private boolean isStudyApply;

    @ApiModelProperty(name = "currentCapacity", value = "현재 인원", example = "1")
    private int currentCapacity;

    @ApiModelProperty(name = "maxCapacity", value = "총 인원", example = "5")
    private int maxCapacity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(name = "messages", value = "채팅 메세지 리스트")
    private List<ChatMessageDTO> messages;
}

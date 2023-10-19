package com.example.talktudy.dto.team;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamResponse {
    @ApiModelProperty(name = "teamId", value = "팀 채팅방 아이디", example = "1")
    private int teamId;

    @ApiModelProperty(name = "title", value = "채팅방 팀 타이틀", example = "취업 정보 공유합니다")
    private String title;

    @ApiModelProperty(name = "interests", value = "관심 분야, [NONE, FRONTEND, BACKEND, DESIGNER, PM, ANDROID, IOS, FULLSTACK] 8개로 고정.", example = "FRONTEND")
    private String interests;

    @ApiModelProperty(name = "description", value = "모집 소개. 에디터 사용으로 Html 문자열.", example = "<p>예시내용</p><strong>강조표시</strong>")
    private String description;

    @ApiModelProperty(name = "tag", value = "태그. ,로 구분. 공백X", example = "Typescript,java")
    private String tag;

    @ApiModelProperty(name = "views", value = "조회수", example = "13")
    private int views;

    @ApiModelProperty(name = "nickname", value = "작성자, 개설자의 닉네임", example = "닉네임123")
    private String nickname;

    @ApiModelProperty(name = "createdDate", value = "채팅방 모집 글 등록 날짜, 년월일", example = "2023-10-10")
    private LocalDateTime createdDate;

    @ApiModelProperty(name = "updatedDate", value = "채팅방 모집 글 수정 날짜, 년월일", example = "2023-10-10")
    private LocalDateTime updatedDate;
} // end class

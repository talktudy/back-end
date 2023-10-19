package com.example.talktudy.dto.team;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamRequest {
    @ApiModelProperty(name = "title", value = "채팅방 팀 타이틀", example = "취업 정보 공유합니다")
    private String title;

    @ApiModelProperty(name = "interests", value = "관심 분야, [NONE, FRONTEND, BACKEND, DESIGNER, PM, ANDROID, IOS, FULLSTACK] 8개로 고정.", example = "FRONTEND")
    private String interests;

    @ApiModelProperty(name = "description", value = "모집 소개. 에디터 사용으로 Html 문자열.", example = "<p>예시내용</p><strong>강조표시</strong>")
    private String description;

    @ApiModelProperty(name = "tag", value = "태그. ,로 구분. 공백X", example = "Typescript,java")
    private String tag;
} // end class

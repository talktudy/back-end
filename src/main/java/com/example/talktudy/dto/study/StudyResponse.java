package com.example.talktudy.dto.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyResponse {
    @ApiModelProperty(name = "title", value = "스터디 타이틀", example = "풀스택 스터디 모집합니다.")
    private String title;

    @ApiModelProperty(name = "interests", value = "관심 분야, [NONE, FRONTEND, BACKEND, DESIGNER, PM, ANDROID, IOS, FULLSTACK] 8개로 고정.", example = "FRONTEND")
    private String interests;

    @ApiModelProperty(name = "startDate", value = "스터디 모집 시작 날짜, 년월일", example = "2023-10-10")
    private LocalDateTime startDate;

    @ApiModelProperty(name = "endDate", value = "스터디 모집 마감 날짜, 년월일", example = "2023-10-12")
    private LocalDateTime endDate;

    @ApiModelProperty(name = "maxCapacity", value = "총 모집 인원수", example = "5")
    private int maxCapacity;

    @ApiModelProperty(name = "currentCapacity", value = "현재 모집된 인원수", example = "2")
    private int currentCapacity;

    @ApiModelProperty(name = "description", value = "모집 소개. 에디터 사용으로 Html 문자열.", example = "<p>예시내용</p><strong>강조표시</strong>")
    private String description;

    @ApiModelProperty(name = "tag", value = "태그. ,로 구분. 공백X", example = "Typescript,java")
    private String tag;

    @ApiModelProperty(name = "isOpen", value = "모집 상태, true면 모집중, false면 모집완료", example = "true")
    private boolean isOpen;

    @ApiModelProperty(name = "views", value = "조회수", example = "13")
    private int views;

    @ApiModelProperty(name = "nickname", value = "작성자, 개설자의 닉네임", example = "닉네임123")
    private String nickname;
} // end class

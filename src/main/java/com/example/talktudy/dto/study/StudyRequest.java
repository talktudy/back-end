package com.example.talktudy.dto.study;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyRequest {
    @ApiModelProperty(name = "title", value = "스터디 타이틀", example = "풀스택 스터디 모집합니다.")
    private String title;

    @ApiModelProperty(name = "interests", value = "관심 분야, [NONE, FRONTEND, BACKEND, DESIGNER, PM, ANDROID, IOS, FULLSTACK] 8개로 고정.", example = "FRONTEND")
    private String interests;

    @ApiModelProperty(name = "endDate", value = "스터디 모집 마감 날짜, 년월일", example = "2023-10-12")
    private LocalDateTime endDate;

    @ApiModelProperty(name = "maxCapacity", value = "총 모집 인원수", example = "5")
    private int maxCapacity;

    @ApiModelProperty(name = "description", value = "모집 소개. 에디터 사용으로 Html 문자열.", example = "<p>예시내용</p><strong>강조표시</strong>")
    private String description;

    @ApiModelProperty(name = "tag", value = "태그. ,로 구분. 공백X", example = "Typescript,java")
    private String tag;
} // end class

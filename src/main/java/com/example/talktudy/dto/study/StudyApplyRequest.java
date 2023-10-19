package com.example.talktudy.dto.study;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudyApplyRequest {
    @ApiModelProperty(name = "applyText", value = "지원시 작성하는 텍스트", example = "안녕하세요, 스터디를 지원합니다.")
    private String applyText;
}

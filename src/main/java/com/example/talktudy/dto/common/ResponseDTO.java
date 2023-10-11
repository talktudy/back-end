package com.example.talktudy.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class ResponseDTO {
    @ApiModelProperty(example = "응답 코드")
    private final int code;

    @ApiModelProperty(example = "응답 상태")
    private final HttpStatus status;

    @ApiModelProperty(example = "응답 메세지")
    private final String message;
}

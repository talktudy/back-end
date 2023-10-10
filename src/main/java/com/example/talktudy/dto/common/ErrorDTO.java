package com.example.talktudy.dto.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@Setter
public class ErrorDTO {
    @ApiModelProperty(example = "응답 코드")
    private final HttpStatus status;

    @ApiModelProperty(example = "응답 메세지")
    private final String message;
}

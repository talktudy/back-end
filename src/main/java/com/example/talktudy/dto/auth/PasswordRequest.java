package com.example.talktudy.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PasswordRequest {
    @ApiModelProperty(name = "oldPassword", value = "기존 비밀번호", example = "test1234")
    private String oldPassword;

    @ApiModelProperty(name = "newPassword", value = "새로 변경하는 비밀번호", example = "test12345")
    private String newPassword;
}

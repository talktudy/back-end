package com.example.talktudy.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @ApiModelProperty(name = "email", value = "이메일", example = "test@test.com")
    private String email;

    @ApiModelProperty(name = "password", value = "패스워드", example = "test1234")
    private String password;
}

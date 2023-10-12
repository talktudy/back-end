package com.example.talktudy.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class LoginRequest {
    @ApiParam(value = "이메일", required = true)
    @ApiModelProperty(name = "email", value = "이메일", example = "test@test.com")
    private String email;

    @ApiParam(value = "비밀번호", required = true)
    @ApiModelProperty(name = "password", value = "패스워드", example = "test1234")
    private String password;
}

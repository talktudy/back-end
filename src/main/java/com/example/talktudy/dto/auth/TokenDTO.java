package com.example.talktudy.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    @ApiModelProperty(name = "grantType", value = "토큰 grantType", example = "Bearer")
    private String grantType;

    @ApiModelProperty(name = "accessToken", value = "accessToken", example = "eyJhbGciOiJIUzI1NiJ9")
    private String accessToken;

    @ApiModelProperty(name = "refreshToken", value = "refreshToken", example = "eyJhbGciOiJIUzI1NiJ9")
    private String refreshToken;

    @ApiModelProperty(name = "email", value = "이메일", example = "test@test.com")
    private String email;
}

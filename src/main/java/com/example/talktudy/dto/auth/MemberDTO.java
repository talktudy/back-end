package com.example.talktudy.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberDTO {
    @ApiModelProperty(name = "email", value = "이메일", example = "test@test.com")
    private String email;

    @JsonIgnore
    @ApiModelProperty(name = "password", value = "패스워드", example = "test1234")
    private String password;

    @ApiModelProperty(name = "nickname", value = "닉네임, 길이제한 10자", example = "테스트닉네임")
    private String nickname;

    @ApiModelProperty(name = "interests", value = "관심 분야", example = "프론트엔드 개발자")
    private String interests;

    @JsonIgnore
    @ApiModelProperty(name = "profileImage", value = "프로필 이미지, 파일", example = "file")
    private MultipartFile profileImage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(name = "profileImageUrl", value = "프로필 이미지, 주소", example = "http://picutre.com")
    private String profileImageUrl;

    @ApiModelProperty(name = "description", value = "간단 소개", example = "간단한 자기소개 입니다.")
    private String description;

    @JsonIgnore
    @ApiModelProperty(name = "portfolio", value = "포트폴리오, 파일, null 가능", example = "file")
    private MultipartFile portfolio;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(name = "portfolioUrl", value = "포트폴리오, 주소", example = "http://portfolio.com")
    private String portfolioUrl;
}
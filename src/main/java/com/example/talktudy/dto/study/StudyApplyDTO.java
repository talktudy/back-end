package com.example.talktudy.dto.study;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudyApplyDTO {
    @ApiModelProperty(name = "memberId", value = "신청 리스트 회원의 아이디", example = "1")
    private Long memberId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true, name = "profileImageUrl", value = "프로필 이미지, 주소", example = "http://picutre.com")
    private String profileImageUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(name = "interests", value = "관심 분야, [NONE, FRONTEND, BACKEND, DESIGNER, PM, ANDROID, IOS, FULLSTACK] 8개로 고정.", example = "FRONTEND")
    private String interests;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(name = "nickname", value = "닉네임, 길이제한 10자", example = "테스트닉네임")
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(name = "applyText", value = "지원시 작성하는 텍스트", example = "안녕하세요, 스터디를 지원합니다.")
    private String applyText;

    @ApiModelProperty(name = "applyStatus", value = "지원 현황(대기중-PENDING, 수락됨-ACCEPTED, 거절됨-REJECTED)", example = "PENDING")
    private String applyStatus;


}

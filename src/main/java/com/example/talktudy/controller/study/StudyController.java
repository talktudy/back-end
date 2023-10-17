package com.example.talktudy.controller.study;

import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.study.StudyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/study")
@RestController
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @ApiOperation("스터디 등록 api - 헤더에 토큰을 받고, 스터디 정보를 입력받아 스터디 모집 등록")
    @PostMapping(value = "/register")
    public ResponseEntity<StudyResponse> registerStudy(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody StudyRequest studyRequest) {
        return ResponseEntity.ok(studyService.registerStudy(customUserDetails.getMemberId(), studyRequest));
    }


} // end class

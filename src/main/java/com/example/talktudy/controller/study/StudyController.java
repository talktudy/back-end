package com.example.talktudy.controller.study;

import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.study.StudyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RequestMapping("/api/study")
@RestController
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @ApiOperation("스터디 등록 api - 헤더에 토큰을 받고, 스터디 정보를 입력받아 스터디 모집 등록")
    @PostMapping(value = "/register")
    public ResponseEntity<StudyResponse> registerStudy(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody StudyRequest studyRequest) {
        return ResponseEntity.ok(studyService.registerStudy(customUserDetails.getMemberId(), studyRequest));
    }

    @ApiOperation("스터디 리스트 조회 api - 모든 스터디 리스트 조회, 페이지네이션 가능")
    @GetMapping
    public ResponseEntity<Page<StudyResponse>> getStudyList(Pageable pageable) {
        return ResponseEntity.ok(studyService.getStudyList(pageable));
    }


} // end class

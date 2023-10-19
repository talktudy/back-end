package com.example.talktudy.controller.study;

import com.example.talktudy.dto.study.StudyApplyDTO;
import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.study.StudyMemberService;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@RequestMapping("/api/study/apply")
@RestController
@RequiredArgsConstructor
public class StudyMemberController {
    private final StudyMemberService studyMemberService;

    @ApiOperation("스터디 지원 신청 api - 헤더에 토큰을 받고, 스터디 지원 신청 정보를 입력받아 등록, 존재하면 수정")
    @PostMapping(value = "/{studyId}")
    public ResponseEntity<StudyApplyDTO> registerStudyApply(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long studyId, @RequestBody StudyApplyDTO studyRequest) {
        return ResponseEntity.ok(studyMemberService.registerStudyApply(customUserDetails.getMemberId(), studyId, studyRequest.getApplyText()));
    }

    @ApiOperation(value = "스터디 지원 현황 조회 api - 헤더에 토큰을 받고, 개설자는 모든 스터디 지원 현황을 조회", notes = "")
    @GetMapping("/{studyId}")
    public ResponseEntity<List<StudyApplyDTO>> getStudyApplyList(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(studyMemberService.getStudyApplyList(customUserDetails.getMemberId(), studyId));
    }

}

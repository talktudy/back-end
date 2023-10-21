package com.example.talktudy.controller.study;

import com.example.talktudy.dto.study.StudyApplyDTO;
import com.example.talktudy.dto.study.StudyApplyRequest;
import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.study.StudyMemberService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RequestMapping("/api/study/apply")
@RestController
@RequiredArgsConstructor
@Api(tags = {"StudyMember API - 스터디 신청, 지원과 관련된 Api"})
public class StudyMemberController {
    private final StudyMemberService studyMemberService;

    @ApiOperation("스터디 지원 신청 api - 헤더에 토큰을 받고, 스터디 지원 신청 정보를 입력받아 등록, 존재하면 수정")
    @PostMapping(value = "/{studyId}")
    @ApiResponse(code = 400, message = "개설자가 스터디 신청시 예외 처리")
    public ResponseEntity<StudyApplyDTO> registerStudyApply(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long studyId,
            @RequestBody StudyApplyRequest studyApplyRequest) {
        return ResponseEntity.ok(studyMemberService.registerStudyApply(customUserDetails.getMemberId(), studyId, studyApplyRequest.getApplyText()));
    }

    @ApiOperation(value = "스터디 지원 현황 조회 api - 헤더에 토큰을 받고, 개설자는 모든 스터디 지원 현황을 조회", notes = "")
    @GetMapping("/{studyId}")
    @ApiResponse(code = 401, message = "토큰이 개설자가 아닐 경우 인증 예외 처리")
    public ResponseEntity<List<StudyApplyDTO>> getStudyApplyList(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long studyId
    ) {
        return ResponseEntity.ok(studyMemberService.getStudyApplyList(customUserDetails.getMemberId(), studyId));
    }

    @ApiOperation(value = "스터디 지원 상태 변경 api - 헤더에 토큰을 받고, 개설자는 스터디 지원한 회원을 수락, 거절, 대기로 변경", notes = "지원 현황(대기중-PENDING, 수락됨-ACCEPTED, 거절됨-REJECTED). 모든 대기중 상태에서만 수락과 거절이 가능하며, 수락과 대기중 상태일때는 모두 대기중으로만 갈 수 있다. study의 currentCapacity가 수락되면 증가한다.")
    @PutMapping("/{studyId}")
    @ApiResponse(code = 401, message = "토큰이 개설자가 아닐 경우 인증 예외 처리")
    public ResponseEntity<StudyApplyDTO> changeApplyStatus(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Long studyId,
            @RequestBody StudyApplyDTO studyApplyRequest
    ) {
        return ResponseEntity.ok(studyMemberService.changeApplyStatus(customUserDetails.getMemberId(), studyId, studyApplyRequest.getMemberId(), studyApplyRequest.getApplyStatus()));
    }

}

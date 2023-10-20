package com.example.talktudy.controller.study;

import com.example.talktudy.dto.common.ResponseDTO;
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

    @ApiOperation("스터디 수정 api - 헤더에 토큰을 받고, 스터디 정보를 입력받아 스터디 글 수정")
    @PutMapping(value = "/update/{studyId}")
    public ResponseEntity<StudyResponse> updateStudy(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long studyId, @RequestBody StudyRequest studyRequest) {
        return ResponseEntity.ok(studyService.updateStudy(customUserDetails.getMemberId(), studyId, studyRequest));
    }

    @ApiOperation(value = "스터디 리스트 조회 api - 모든 스터디 리스트 조회, 페이지네이션 가능", notes = "쿼리스트링으로 orderby=views(maxCapacity, endDate..)로 내림차순 조회 가능. 페이지네이션 : size=4, page=1..")
    @GetMapping
    public ResponseEntity<Page<StudyResponse>> getStudyList(
            Pageable pageable,
            @RequestParam(required = false, value = "orderby") String orderCriteria, // 조회수
            @RequestParam(required = false, value = "open") String isOpen, // 모집중의 여부
            @RequestParam(required = false, value = "interests") List<String> interests, // 다중으로 선택하는 모집분야별
            @RequestParam(required = false, value = "keyword") String keyword, // 검색 키워드
            @RequestParam(required = false, value = "type") String type // 태그 Or 제목
    ) {
        return ResponseEntity.ok(studyService.getStudyList(pageable, orderCriteria, isOpen, interests, keyword, type));
    }

    @ApiOperation("스터디 리스트 모집중 별 조회 api - 모든 스터디 리스트 모집중 상태만 조회, 페이지네이션 가능. 페이지네이션 : size=4, page=1..")
    @GetMapping("/open")
    public ResponseEntity<Page<StudyResponse>> getStudyListByOpen(Pageable pageable) {
        return ResponseEntity.ok(studyService.getStudyListByOpen(pageable));
    }

    @ApiOperation("스터디 단일 조회 api - 스터디 상세 조회")
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyResponse> getStudy(@PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.getStudy(studyId));
    }

    @ApiOperation("스터디 모집 마감 api - 토큰의 헤더를 받고, 스터디의 모집을 마감한다.")
    @PostMapping("/{studyId}/close")
    public ResponseEntity<ResponseDTO> closeStudy(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long studyId) {
        return ResponseEntity.ok(studyService.closeStudy(customUserDetails.getMemberId(), studyId));
    }


} // end class

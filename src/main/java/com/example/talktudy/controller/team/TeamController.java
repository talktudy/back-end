package com.example.talktudy.controller.team;

import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.dto.team.TeamRequest;
import com.example.talktudy.dto.team.TeamResponse;
import com.example.talktudy.security.CustomUserDetails;
import com.example.talktudy.service.team.TeamService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @ApiOperation("채팅방 등록 api - 헤더에 토큰을 받고, 채팅 팀 정보를 입력받아 채팅방 등록")
    @PostMapping(value = "/register")
    public ResponseEntity<TeamResponse> registerTeam(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody TeamRequest teamRequest) {
        return ResponseEntity.ok(teamService.registerTeam(customUserDetails.getMemberId(), teamRequest));
    }

    @ApiOperation("채팅방 수정 api - 헤더에 토큰을 받고, 채팅 팀 정보를 입력받아 채팅방 글 수정")
    @PutMapping(value = "/update/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(@ApiIgnore @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long teamId, @RequestBody TeamRequest teamRequest) {
        return ResponseEntity.ok(teamService.updateTeam(customUserDetails.getMemberId(), teamId, teamRequest));
    }

    @ApiOperation(value = "채팅방 리스트 조회 api - 모든 채팅팀 리스트 조회, 페이지네이션 가능", notes = "쿼리스트링으로 orderby=views(endDate..)로 내림차순 조회 가능. 페이지네이션 : size=4, page=1..")
    @GetMapping
    public ResponseEntity<Page<TeamResponse>> getTeamList(
            Pageable pageable,
            @RequestParam(required = false, value = "orderby") String orderCriteria
            ) {
        return ResponseEntity.ok(teamService.getTeamList(pageable, orderCriteria));
    }

    @ApiOperation("채팅방 단일 조회 api - 채팅방 상세 조회")
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }






} //  end class

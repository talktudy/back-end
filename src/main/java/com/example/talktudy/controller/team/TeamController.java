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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
} //  end class

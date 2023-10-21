package com.example.talktudy.service.team;

import com.example.talktudy.dto.chat.ChatRoomDTO;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.dto.team.TeamRequest;
import com.example.talktudy.dto.team.TeamResponse;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.repository.chat.ChatRoomRepository;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.tag.TagRepository;
import com.example.talktudy.repository.team.*;
import com.example.talktudy.service.study.StudyMapper;
import com.example.talktudy.service.tag.TagService;
import com.example.talktudy.service.tag.TeamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TagService tagService;
    private final ChatRoomRepository chatRoomRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public TeamResponse registerTeam(Long memberId, TeamRequest teamRequest) {

        // 1. DB에서 회원을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. Team 객체를 만든다.
        Team team = Team.builder()
                .member(member)
                .title(teamRequest.getTitle())
                .interests(Enum.valueOf(Interests.class, teamRequest.getInterests()))
                .description(teamRequest.getDescription())
                .build();

        // 3. Tag 테이블에서 기존 값이 있는지 검사한다. 값이 있으면 tag에는 저장하고 없으면 저장한다.
        // 그렇게 만들어진 Tag 값을 teamTag에 저장한다.
        Set<TeamTag> teamTags = tagService.createTeamTags(teamRequest.getTag().split(","), team, false);

        String teamTagsStr = teamTags.stream()
                .map(teamTag -> teamTag.getTag().getName()) // 또는 다른 필드를 선택
                .collect(Collectors.joining(","));

        team.setTeamTags(teamTags);

        // 4. 채팅방을 생성한다.
        ChatRoom chatRoom = ChatRoom.builder()
                .name(team.getTitle())
                .isStudyApply(false)
                .team(team)
                .maxCapacity(10)
                .build();

        chatRoomRepository.save(chatRoom);

        // 5. Team 객체를 DB에 등록한다.
        Team newTeam = teamRepository.save(team);

        // 6. Entity -> DTO 매핑한다.
        return TeamMapper.INSTANCE.teamEntityToDto(newTeam, teamTagsStr, newTeam.getMember().getNickname());
    }

    @Transactional
    public TeamResponse updateTeam(Long memberId, Long teamId, TeamRequest teamRequest) {
        // 1. DB에서 개설자 회원과 팀을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        Team team = teamRepository.findById(teamId).orElseThrow(() -> new CustomNotFoundException("팀 정보를 찾을 수 없습니다."));

        // 2. 해당 회원이 팀의 개설자인지 확인한다.
        if (!member.equals(team.getMember())) throw new BadCredentialsException("접근 권한이 없습니다.");

        // 3. 팀 정보를 업데이트 한다.
        team.setTitle(teamRequest.getTitle() != null ? teamRequest.getTitle() : team.getTitle());
        team.setDescription(teamRequest.getDescription() != null ? teamRequest.getDescription() : team.getDescription());
        team.setInterests(teamRequest.getInterests() != null ? Enum.valueOf(Interests.class, teamRequest.getInterests()) : team.getInterests());

        // 4. 태그 정보를 업데이트 한다.
        Set<TeamTag> teamTags = tagService.createTeamTags(teamRequest.getTag().split(","), team, true);

        String teamTagsStr = teamTags.stream()
                .map(teamTag -> teamTag.getTag().getName()) // 또는 다른 필드를 선택
                .collect(Collectors.joining(","));

        team.setTeamTags(teamTags);

        // 5. TODO : 팀 채팅방의 정보도 변경?

        // 8. team 객체를 DB에 등록한다.
        Team newTeam = teamRepository.save(team);

        // 6. Entity -> DTO 매핑한다.
        return TeamMapper.INSTANCE.teamEntityToDto(newTeam, teamTagsStr, newTeam.getMember().getNickname());
    }

    @Transactional(readOnly = true)
    public Page<TeamResponse> getTeamList(Pageable pageable, String orderCriteria, List<String> interests, String keyword, String type) {

        // 1. QueryDSL로 동적 조회 쿼리
        Page<Team> teamPage = teamRepository.findAll(pageable, orderCriteria, interests, keyword, type);

        // 2. 응답 형태로 변환해 리턴한다.
        List<TeamResponse> teamResponses = teamPage.stream()
                .map(team -> TeamMapper.INSTANCE.teamEntityToDto(team, team.getTagNamesAsString(), team.getMember().getNickname()))
                .collect(Collectors.toList());

        return new PageImpl<>(teamResponses, pageable, teamResponses.size());
    }

    @Transactional(readOnly = true)
    public TeamResponse getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new CustomNotFoundException("채팅팀 정보를 찾을 수 없습니다."));

        // 조회수 증가
        team.setViews(team.getViews() + 1);
        Team newTeam = teamRepository.save(team);

        return TeamMapper.INSTANCE.teamEntityToDto(newTeam, newTeam.getTagNamesAsString(), newTeam.getMember().getNickname());
    }

} // end class

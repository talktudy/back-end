package com.example.talktudy.service.team;

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
import com.example.talktudy.repository.team.Team;
import com.example.talktudy.repository.team.TeamRepository;
import com.example.talktudy.repository.team.TeamTagRepository;
import com.example.talktudy.service.study.StudyMapper;
import com.example.talktudy.service.tag.TagService;
import com.example.talktudy.service.tag.TeamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TagService tagService;
    private final ChatRoomRepository chatRoomRepository;

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
        tagService.createTeamTags(teamRequest.getTag().split(","), team);

        // 4. 채팅방을 생성한다.
        ChatRoom chatRoom = ChatRoom.builder()
                .name(team.getTitle())
                .isStudyApply(false)
                .team(team)
                .build();

        chatRoomRepository.save(chatRoom);

        // 5. Team 객체를 DB에 등록한다.
        Team newTeam = teamRepository.save(team);

        // 6. Entity -> DTO 매핑한다.
        return TeamMapper.INSTANCE.teamEntityToDto(newTeam, teamRequest.getTag(), member.getNickname());
    }

    @Transactional(readOnly = true)
    public Page<TeamResponse> getTeamList(Pageable pageable) {
        // 1. DB에서 팀 리스트를 찾는다
        Page<Team> teamPage = teamRepository.findAll(pageable);

        // 2. 응답 형태로 변환해 리턴한다.
        List<TeamResponse> teamResponses = teamPage.stream()
                .map(team -> TeamMapper.INSTANCE.teamEntityToDto(team, team.getTagNamesAsString(), team.getMember().getNickname()))
                .collect(Collectors.toList());

        return new PageImpl<>(teamResponses, pageable, teamResponses.size());
    }
} // end class

package com.example.talktudy.service.study;

import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.repository.chat.ChatRoomRepository;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.study.*;
import com.example.talktudy.repository.tag.Tag;
import com.example.talktudy.repository.tag.TagRepository;
import com.example.talktudy.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {
    private final ChatRoomRepository chatRoomRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyTagRepository studyTagRepository;
    private final TagRepository tagRepository;

    @Transactional
    public StudyResponse registerStudy(Long memberId, StudyRequest studyRequest) {

        // 1. DB에서 회원을 찾는다.
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));

        // 2. Study 객체를 만든다.
        Study study = Study.builder()
                .member(member) // 개설자
                .title(studyRequest.getTitle()) // 제목
                .interests(Enum.valueOf(Interests.class, studyRequest.getInterests()))
                .isOpen(true) // 첫 스터디 등록시 무조건 모집중
                .description(studyRequest.getDescription())
                .maxCapacity(studyRequest.getMaxCapacity()) // 모집 인원
                .currentCapacity(0) // 등록시 무조건 0
                .endDate(studyRequest.getEndDate()) // 마감 예정일
                .build();

        // 3. Tag 테이블에서 기존 값이 있는지 검사한다. 값이 있으면 tag에는 저장하고 없으면 저장한다.
        // 그렇게 만들어진 Tag 값을 StudyTag에 저장한다.
        String[] tags = studyRequest.getTag().split(",");

        List<StudyTag> studyTags = Arrays.stream(tags).map(
                tagName -> {
                    Tag tag = tagRepository.findByName(tagName).orElseGet(
                            () -> tagRepository.save(
                                    Tag.builder().name(tagName).build()
                            )
                    );

                    return StudyTag.builder().study(study).tag(tag).build();
                }
        ).collect(Collectors.toList());

        studyTagRepository.saveAll(studyTags);

        // 4. TODO : 지원, 팀 채팅방을 개설한다.
        // 지원
        ChatRoom applyChatRoom = ChatRoom.builder()
                .name(study.getTitle())
                .isStudyApply(true)
                .study(study)
                .build();

        // 팀 채팅방
        ChatRoom chatRoom = ChatRoom.builder()
                .name(study.getTitle())
                .isStudyApply(false)
                .study(study)
                .build();

        chatRoomRepository.save(applyChatRoom);
        chatRoomRepository.save(chatRoom);

        // 5. Study 객체를 DB에 등록한다.
        Study newStudy = studyRepository.save(study);

        // 6. Entity -> DTO 매핑한다.
        return StudyMapper.INSTANCE.studyEntityToDto(newStudy, studyRequest.getTag(), member.getNickname());
    }
} // end class

package com.example.talktudy.service.study;

import com.example.talktudy.dto.study.StudyRequest;
import com.example.talktudy.dto.study.StudyResponse;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.study.*;
import com.example.talktudy.repository.tag.Tag;
import com.example.talktudy.repository.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyTagRepository studyTagRepository;
    private final TagRepository tagRepository;

    @Transactional
    public StudyResponse registerStudy(Long memberId, StudyRequest studyRequest) {
        // 1. DB에서 회원을 찾는다.
//        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("회원을 찾을 수 없습니다."));
//
//        // 2. Study 객체를 만든다.
//        Study study = Study.builder()
//                .member(member) // 개설자
//                .title(studyRequest.getTitle()) // 제목
//                .interests(Enum.valueOf(Interests.class, studyRequest.getInterests()))
//                .isOpen(true) // 첫 스터디 등록시 무조건 모집중
//                .description(studyRequest.getDescription())
//                .maxCapacity(studyRequest.getMaxCapacity() + 1) // 총 인원수
//                .currentCapacity(1) // 등록시 무조건 본인 인원수 +1
//                .endDate(studyRequest.getEndDate()) // 마감 예정일
//                .build();
//
//        // 3. Tag를 tag, study_tag 객체를 생성한다.
//        // TODO : 기존에 존재하는 태그를 검사해야한다.
//        Tag tag = Tag.builder().name(studyRequest.getTag()).build();
//
//        StudyTag studyTag = StudyTag.builder()
//                .study(study)
//                .tag(tag)
//                .build();
//
//        // 4. 지원, 팀 채팅방을 개설한다.
//
//        // 5. DB에 등록한다.
//        tagRepository.save(tag);
//        studyRepository.save(study);


        // 6. Entity -> DTO 매핑한다.

        return null;
    }
} // end class

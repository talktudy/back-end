package com.example.talktudy.repository.study;

import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class StudyRepositoryTest {
    @Autowired
    private StudyRepository studyRepository;
    private MemberRepository memberRepository;

    @DisplayName("StudyRepository - 스터디 등록 테스트")
    @Test
    void save() {
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
    }
}
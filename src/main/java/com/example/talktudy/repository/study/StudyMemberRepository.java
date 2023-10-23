package com.example.talktudy.repository.study;

import com.example.talktudy.repository.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findAllByStudy(Study study);
    StudyMember findByStudy(Study study);
    List<StudyMember> findAllByMember(Member member);
}

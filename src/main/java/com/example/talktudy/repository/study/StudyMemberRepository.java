package com.example.talktudy.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findAllByStudy(Study study);
    StudyMember findByStudy(Study study);
}

package com.example.talktudy.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyTagRepository extends JpaRepository<StudyTag, Long> {
    void deleteAllByStudy(Study study);
}

package com.example.talktudy.repository.study;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyTagRepository extends JpaRepository<StudyTag, Long> {
}

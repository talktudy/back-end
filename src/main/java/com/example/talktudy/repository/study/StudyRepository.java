package com.example.talktudy.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    Page<Study> findAllByOpenTrue(Pageable pageable);
    Page<Study> findAllByOpenFalse(Pageable pageable);
    List<Study> findAllByEndDate(LocalDateTime endDate);
}

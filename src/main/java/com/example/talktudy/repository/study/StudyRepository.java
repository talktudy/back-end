package com.example.talktudy.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    Page<Study> findAllByOpenTrue(Pageable pageable);
    Page<Study> findAllByOpenFalse(Pageable pageable);
}

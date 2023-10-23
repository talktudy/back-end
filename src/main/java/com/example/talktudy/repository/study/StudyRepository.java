package com.example.talktudy.repository.study;

import com.example.talktudy.repository.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, CustomStudyRepository {
    Page<Study> findAllByOpenTrue(Pageable pageable);
    Page<Study> findAllByOpenFalse(Pageable pageable);
    List<Study> findAllByEndDate(LocalDateTime endDate);
    List<Study> findAllByMember(Member member);


}

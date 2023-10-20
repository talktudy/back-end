package com.example.talktudy.repository.study;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Predicate;

public interface CustomStudyRepository {
    Page<Study> findAll(Pageable pageable, String orderCriteria, String isOpen, List<String> interests, String keyword, String type);
}

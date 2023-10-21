package com.example.talktudy.repository.team;

import com.example.talktudy.repository.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTeamRepository {
    Page<Team> findAll(Pageable pageable, String orderCriteria, List<String> interests, String keyword, String type);
}

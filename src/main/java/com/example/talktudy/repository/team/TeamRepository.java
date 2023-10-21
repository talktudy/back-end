package com.example.talktudy.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>, CustomTeamRepository {
}

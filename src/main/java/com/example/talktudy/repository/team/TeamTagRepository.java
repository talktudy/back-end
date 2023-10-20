package com.example.talktudy.repository.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamTagRepository extends JpaRepository<TeamTag, Long> {
    void deleteAllByTeam(Team team);
}

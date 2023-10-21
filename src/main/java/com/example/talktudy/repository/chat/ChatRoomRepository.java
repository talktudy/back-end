package com.example.talktudy.repository.chat;

import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTeam(Team team);
    Optional<ChatRoom> findByStudy(Study study);
    Optional<ChatRoom> findByStudyAndIsStudyApplyFalse(Study study);
    ChatRoom findByStudyAndIsStudyApplyTrue(Study study);
}

package com.example.talktudy.repository.chat;

import com.example.talktudy.repository.study.Study;
import com.example.talktudy.repository.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByTeam(Team team);
    List<ChatRoom> findAllByStudy(Study study);
    Optional<ChatRoom> findByStudyAndIsStudyApplyFalse(Study study);
    Optional<ChatRoom> findByStudyAndIsStudyApplyTrue(Study study);
}

package com.example.talktudy.repository.chat;

import com.example.talktudy.repository.study.Study;
import lombok.*;

import javax.persistence.*;
import com.example.talktudy.repository.team.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @Column(name = "chat_room_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(fetch = FetchType.LAZY) // 스터디 생성시 채팅룸 생성
    @JoinColumn(name = "study_id")
    private Study study;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "is_study_apply", nullable = false)
    private boolean isStudyApply;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<ChatMessage> messages = new ArrayList<>();

} // end class

package com.example.talktudy.repository.team;

import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team {
    @Id
    @Column(name = "team_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "interests", nullable = false)
    @Enumerated(EnumType.STRING)
    private Interests interests;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "views", nullable = false)
    private int views;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate; // 채팅방 개설 날

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updateDate; // 채팅방 개설글 수정일

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private Set<TeamMember> teamMembers;
} // end class

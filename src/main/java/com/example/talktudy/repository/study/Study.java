package com.example.talktudy.repository.study;

import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "study")
public class Study {
    @Id
    @Column(name = "study_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "interests", nullable = false)
    @Enumerated(EnumType.STRING)
    private Interests interests;

    @Column(name = "status", nullable = false)
    private boolean isOpen; // 모집중이면 True, 모집완료시 false

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "current_capacity", nullable = false)
    private int currentCapacity;

    @Column(name = "views", nullable = false, columnDefinition = "DEFAULT 0 CHECK(amount) >= 0")
    private int views;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate; // 등록된 날짜, 모집 시작일

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updateDate; // 모집글 수정일짜

    // TODO : Sceduled 필요
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate; // 마감예정일

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE)
    private Set<StudyMember> studyMembers;
} // end class

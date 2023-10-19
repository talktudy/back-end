package com.example.talktudy.repository.study;

import com.example.talktudy.repository.common.ApplyStatus;
import com.example.talktudy.repository.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "study_member")
public class StudyMember {
    // study_apply의 apply_status가 Ok로 되었을때, 모집이 되었을때 모집된 멤버 엔티티
    @Id
    @Column(name = "study_member_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "apply_status")
    @Enumerated(EnumType.STRING)
    private ApplyStatus applyStatus;

    @Column(name = "apply_text")
    private String applyText;

}// end class

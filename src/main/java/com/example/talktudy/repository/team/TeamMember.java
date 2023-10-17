//package com.example.talktudy.repository.team;
//
//import com.example.talktudy.repository.common.ApplyStatus;
//import com.example.talktudy.repository.member.Member;
//import com.example.talktudy.repository.study.Study;
//import lombok.*;
//
//import javax.persistence.*;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "team_member")
//public class TeamMember {
//    @Id
//    @Column(name = "team_member_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long teamMemberId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "team_id")
//    private Team team;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//}

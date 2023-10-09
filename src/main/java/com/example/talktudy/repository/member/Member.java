package com.example.talktudy.repository.member;

import com.example.talktudy.repository.common.Interests;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "interests", nullable = false)
    @Enumerated(EnumType.STRING)
    private Interests interests;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    @Lob
    @Column(name = "portfolio")
    private String portfolio;

    // TODO : 탈퇴 여부 논의 필요!
//    @Column(name = "portfolioUrl", nullable = false)
//    private boolean withdrawal;
}

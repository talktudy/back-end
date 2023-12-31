package com.example.talktudy.repository.member;

import com.example.talktudy.repository.common.Interests;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

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
    @Column(name = "portfolioUrl")
    private String portfolioUrl;

    // TODO : 탈퇴 여부 논의 필요!
//    @Column(name = "portfolioUrl", nullable = false)
//    private boolean withdrawal;

    // 객체 주소 비교가 아닌 객체의 아이디가 동등한지 비교로 재정의 하기 위해 오버라이딩
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return Objects.equals(this.getMemberId(), member.getMemberId());
    }
}

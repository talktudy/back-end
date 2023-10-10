package com.example.talktudy.repository.token;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refreshToken")
public class RefreshToken {
    @Id
    @Column(name = "refreshToken_id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(name = "refreshToken", nullable = false)
    private String refreshToken;

    @Column(name = "email", nullable = false)
    private String email;

} // end class

package com.example.talktudy.service.auth;

import com.example.talktudy.dto.auth.MemberRequest;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.member.Role;
import com.example.talktudy.security.JwtTokenProvider;
import com.example.talktudy.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final S3Uploader s3Uploader;

    @Value("${default.profile.image}")
    private String defaultProfileImage;

    @Transactional
    public boolean register(MemberRequest memberRequest) {
        String email = memberRequest.getEmail();

        // 1. DB에 해당 이메일의 회원이 존재하는지 검사
        if (memberRepository.existsByEmail(email)) {
            return false;
        }

        // 2. 회원 객체 생성
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .role(email.equals("admin@admin.com") ? Role.ROLE_ADMIN : Role.ROLE_USER)
                .nickname(memberRequest.getNickname())
                .interests(Enum.valueOf(Interests.class, memberRequest.getInterests()))
                .description(memberRequest.getDescription())
                .build();

        // 3. 프로필 이미지를 S3에 업로드(없으면 기본 이미지)
        member.setProfileImageUrl(
                memberRequest.getProfileImage() == null ?
                        defaultProfileImage : s3Uploader.uploadFile(memberRequest.getProfileImage(), S3Uploader.S3_DIR_MEMBER)
        );

        // 4. 포트폴리오를 S3에 업로드
        if (memberRequest.getPortfolio() != null) {
            member.setPortfolio(s3Uploader.uploadFile(memberRequest.getPortfolio(), S3Uploader.S3_DIR_PORTFOLIO));
        }

        // 5. save 쿼리
        return memberRepository.save(member) != null;
    }

} // end class

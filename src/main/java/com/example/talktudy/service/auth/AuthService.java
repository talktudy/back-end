package com.example.talktudy.service.auth;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.talktudy.dto.auth.LoginRequest;
import com.example.talktudy.dto.auth.MemberDTO;
import com.example.talktudy.dto.auth.TokenDTO;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.exception.CustomNotFoundException;
import com.example.talktudy.repository.token.RefreshToken;
import com.example.talktudy.repository.token.RefreshTokenRepository;
import com.example.talktudy.repository.common.Interests;
import com.example.talktudy.repository.member.Member;
import com.example.talktudy.repository.member.MemberRepository;
import com.example.talktudy.repository.member.Role;
import com.example.talktudy.security.JwtTokenProvider;
import com.example.talktudy.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    private final S3Uploader s3Uploader;

    @Value("${default.profile.image}")
    private String defaultProfileImage;

    @Transactional
    public ResponseDTO register(MemberDTO memberDTO) {
        String email = memberDTO.getEmail();

        // 1. DB에 해당 이메일의 회원이 존재하는지 검사
        if (memberRepository.existsByEmail(email)) {
            throw new CustomNotFoundException("회원이 이미 존재합니다.");
        }

        // 2. 회원 객체 생성
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .role(email.equals("admin@admin.com") ? Role.ROLE_ADMIN : Role.ROLE_USER)
                .nickname(memberDTO.getNickname())
                .interests(Enum.valueOf(Interests.class, memberDTO.getInterests()))
                .description(memberDTO.getDescription())
                .build();

        // 3. 프로필 이미지를 S3에 업로드(없으면 기본 이미지)
        member.setProfileImageUrl(
                memberDTO.getProfileImage() == null ||
                        memberDTO.getProfileImage().isEmpty() ?
                        defaultProfileImage : s3Uploader.uploadFile(memberDTO.getProfileImage(), S3Uploader.S3_DIR_MEMBER)
        );

        // 4. 포트폴리오를 S3에 업로드
        if (memberDTO.getPortfolio() != null) {
            if (memberDTO.getPortfolio().getOriginalFilename().length() > 0) {
                member.setPortfolioUrl(s3Uploader.uploadFile(memberDTO.getPortfolio(), S3Uploader.S3_DIR_PORTFOLIO));
            }
        }

        // 5. save 쿼리
        memberRepository.save(member);

        return ResponseDTO.of(200, HttpStatus.OK, "회원 등록에 성공하였습니다.");

    }

    @Transactional(readOnly = true)
    public ResponseDTO checkEmail(String email) {

        // 1. DB에 해당 이메일의 회원이 존재하는지 검사
        if (memberRepository.existsByEmail(email)) {
            return ResponseDTO.of(409, HttpStatus.CONFLICT, "해당 이메일의 회원이 이미 존재합니다.");
        }
        else {
            return ResponseDTO.of(200, HttpStatus.OK, "해당 이메일로 회원 가입이 가능합니다.");
        }
    }

    @Transactional
    public TokenDTO login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            // 1. 인증정보 검사(DB에서 사용자 정보 확인, 비밀번호 비교 등) 후 SpringSecurity Context에 인증 정보 등록하기
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. 인증 정보 기반으로 JWT access, refresh 토큰 생성하기
            TokenDTO tokenDTO = jwtTokenProvider.createToken(email);

            // 3. Refresh Token 저장하기
            // db에 기존 리프레쉬 토큰이 있다면 수정, 없다면 등록
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByEmail(email);

            if (refreshToken.isPresent()) {
                refreshToken.get().setRefreshToken(tokenDTO.getRefreshToken());
                refreshTokenRepository.save(refreshToken.get());
            }
            else
            {
                refreshTokenRepository.save(
                        RefreshToken.builder()
                                .refreshToken(tokenDTO.getRefreshToken())
                                .email(email)
                                .build());
            }

            return tokenDTO;

        } catch (BadCredentialsException e) {
            throw new CustomNotFoundException("로그인에 실패하였습니다. 회원이 존재하지 않습니다.");
        } catch (Exception e) {
            throw new CustomNotFoundException("로그인에 실패하였습니다. 아이디와 비밀번호를 확인해주세요");
        }
    }

    @Transactional(readOnly = true)
    public TokenDTO reissue(String accessToken, String refreshToken) {
        // 1. 리프레쉬 토큰의 유효성을 검사
        if (!jwtTokenProvider.validateToken(refreshToken, false)) return null;

        // 2. DB에 저장한 토큰 비교
        RefreshToken savedRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh Token이 없습니다."));

        // 3. refresh Token 기반으로 access token 재발급
        String newAccessToken = jwtTokenProvider.createAccessToken(savedRefreshToken.getEmail());

        // 4. 재발급한 access token을 리턴
        return TokenDTO.builder().grantType("Bearer").accessToken(newAccessToken).refreshToken(refreshToken).email(savedRefreshToken.getEmail()).build();
    }

    @Transactional
    public ResponseDTO logout(String accessToken) {
        if (accessToken == null) throw new NullPointerException("토큰 값이 없습니다.");

        // 1. accessToken에서 사용자 정보를 가져온다
        String email = jwtTokenProvider.getUserEmail(accessToken);

        // 2. refreshToken 테이블에서 이메일과 맞는 토큰을 삭제한다.
        if (refreshTokenRepository.findByEmail(email).isPresent()) {
            refreshTokenRepository.deleteByEmail(email);
        } else {
            throw new CustomNotFoundException("테이블에 리프레시 토큰이 없습니다.");
        }

        return ResponseDTO.builder().code(200).status(HttpStatus.OK).message("로그아웃에 성공했습니다.").build();
    }

} // end class

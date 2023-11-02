package com.example.talktudy.security;

import com.example.talktudy.dto.auth.TokenDTO;
import com.example.talktudy.exception.CustomExpiredJwtException;
import com.example.talktudy.repository.token.RefreshToken;
import com.example.talktudy.repository.token.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private Key secretKey;
    private final long accessTokenValidTime = (60 * 1000) * 30; // 30분
    private final long refreshTokenValidTime = (24 * 60 * 60 * 1000) * 3; // 3일
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKeySource.getBytes());
        secretKey = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    // 리퀘스트의 헤더에서 토큰 값 가져오기
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public TokenDTO createToken(String email) {
        Claims claims =  Jwts.claims().setSubject(email);
        Date now = new Date();

        // Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // 만료
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenDTO.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).email(email).build();
    }

    public String createAccessToken(String email) {
        Claims claims =  Jwts.claims().setSubject(email);
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // 만료
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    // 토큰 claims 가져오기
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰이 유효한지 검증
    public boolean validateToken(String token, boolean isAccess) {
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        return true;

//        try {
//
//        } catch (SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//            throw new CustomExpiredJwtException((isAccess ? "[Access]" : "[Refresh]") + "만료된 토큰입니다.");
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//            throw new IllegalArgumentException("토큰 값이 없습니다.");
//        } catch (NullPointerException e){
//            log.error("JWT Token is empty.");
//            throw new NullPointerException("토큰이 null 입니다.");
//        }
//
//        return false;
    }

    // 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserEmail(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 안에 있는 유저 이메일 가져오기
    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

} // end class

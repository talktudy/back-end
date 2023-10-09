package com.example.talktudy.security;

import com.example.talktudy.dto.auth.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import springfox.documentation.service.GrantType;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key-source}")
    private String secretKeySource;
    private Key secretKey;
    // access-token : 30 분, refresh-token : 7일 (테스트는 5분, 10분 하기)
    private final long accessTokenValidTime = (60 * 1000) * 30; // 30분
    private final long refreshTokenValidTime = (60 * 1000) * 60 * 24 * 7; // 7일
    private final CustomUserDetailsService customUserDetailsService;

    @PostConstruct
    protected void init() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKeySource.getBytes());
        secretKey = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    // 리퀘스트의 헤더에서 토큰 값 가져오기
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public TokenDTO createToken(String email, List<String> roles) {
        Claims claims =  Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date now = new Date();

        // Access Token
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간
                .setExpiration(new Date(now.getTime() + accessTokenValidTime)) // 만료
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token
        String refreshToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return TokenDTO.builder().grantType("Bearer").accessToken(accessToken).refreshToken(refreshToken).email(email).build();
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

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        } catch (NullPointerException e){
            log.error("JWT Token is empty.");
        }

        return false;
    }

    // 토큰에서 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 안에 있는 유저 이메일 가져오기
    private String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
} // end class

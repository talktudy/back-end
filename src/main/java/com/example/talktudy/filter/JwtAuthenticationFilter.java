package com.example.talktudy.filter;

import com.example.talktudy.dto.common.ErrorCode;
import com.example.talktudy.dto.common.ResponseDTO;
import com.example.talktudy.exception.CustomExpiredJwtException;
import com.example.talktudy.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서 UserPasswordAuthenticationFilter 로 전달
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = jwtTokenProvider.resolveAccessToken(request);

            if (jwtToken != null) {
                if (!request.getRequestURI().equals("/api/auth/reissue")) {
                    if (jwtTokenProvider.validateToken(jwtToken, true)) {
                        Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            filterChain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            setErrorResponse(response, ErrorCode.EXPIRED_TOKEN);
            // throw new CustomExpiredJwtException((isAccess ? "[Access]" : "[Refresh]") + "만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            setErrorResponse(response, ErrorCode.NULL_TOKEN_VALUE);
        } catch (NullPointerException e) {
            log.error("JWT Token is empty.");
            setErrorResponse(response, ErrorCode.NULL_VALUE);
        }
    }

    // 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    public void setErrorResponse(HttpServletResponse response, ErrorCode error) {
        response.setStatus(error.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(ResponseDTO.of(error.getCode(), error.getHttpStatus(), error.getMeesage()));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
} // end class

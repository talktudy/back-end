package com.example.talktudy.filter;

import com.example.talktudy.dto.common.ErrorCode;
import com.example.talktudy.exception.CustomExpiredJwtException;
import com.example.talktudy.exception.CustomNotAcceptException;
import com.example.talktudy.security.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    // 핸들러를 통과하면 컨트롤러로 넘어가 메세지 매핑을 시작한다.
    // StompHandler는 HttpServletRequest가 아니라 커스텀으로 토큰 처리 해주어야 한다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("STOMP Handler 실행");

        return message;
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//
//        log.info("헤더 : " + message.getHeaders());
//
//        // 헤더에서 토큰 추출
//        String jwtToken = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
//
//        try {
//            if (!jwtTokenProvider.validateToken(jwtToken, true)) {
//                throw new CustomNotAcceptException("유효한 토큰이 아닙니다.");
////                Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);
////                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//
//            log.info("Stomp Handler 실행, jwt token 인증 완료");
//
//        } catch (SecurityException | MalformedJwtException e) {
//            log.info("STOMP : Invalid JWT Token");
//            throw new MalformedJwtException("Invalid JWT Token");
//        } catch (ExpiredJwtException e) {
//            log.info("STOMP : Expired JWT Token", e);
//            throw new CustomExpiredJwtException("Invalid JWT Token");
//        } catch (UnsupportedJwtException e) {
//            log.info("STOMP : Unsupported JWT Token", e);
//            throw new UnsupportedJwtException("Invalid JWT Token");
//        } catch (IllegalArgumentException e) {
//            log.info("STOMP : JWT claims string is empty.", e);
//            throw new IllegalArgumentException("Invalid JWT Token");
//        } catch (NullPointerException e) {
//            log.error("STOMP : JWT Token is empty.");
//            throw new NullPointerException("Invalid JWT Token");
//        }
//
//        return message;
    }
} // end class
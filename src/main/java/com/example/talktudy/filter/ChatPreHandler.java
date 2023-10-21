package com.example.talktudy.filter;

import com.example.talktudy.dto.common.ErrorCode;
import com.example.talktudy.exception.CustomExpiredJwtException;
import com.example.talktudy.exception.CustomNotAcceptException;
import com.example.talktudy.repository.chat.ChatRoom;
import com.example.talktudy.security.JwtTokenProvider;
import com.example.talktudy.service.chat.ChatService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
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
    private final ChatService chatService;

    // 핸들러를 통과하면 컨트롤러로 넘어가 메세지 매핑을 시작한다.
    // StompHandler는 HttpServletRequest가 아니라 커스텀으로 토큰 처리 해주어야 한다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("STOMP Handler 실행");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE == headerAccessor.getCommand()) {
            String destination = headerAccessor.getDestination();
            Long chatRoomId = parseChatRoomId(destination);

            ChatRoom chatRoom = chatService.getRoomById(chatRoomId);

            if (chatRoomId != null) {
                chatRoom.setCurrentCapacity(chatRoom.getCurrentCapacity() + 1);
                chatService.saveChatRoom(chatRoom);

                log.info("ChatRoomId [" + chatRoomId + "] 의 현재 인원 1 입장");
            }
        }
        else if (StompCommand.DISCONNECT == headerAccessor.getCommand()) {
            String destination = headerAccessor.getDestination();
            Long chatRoomId = parseChatRoomId(destination);

            ChatRoom chatRoom = chatService.getRoomById(chatRoomId);

            if (chatRoomId != null) {
                chatRoom.setCurrentCapacity(chatRoom.getCurrentCapacity() - 1);
                chatService.saveChatRoom(chatRoom);

                log.info("ChatRoomId [" + chatRoomId + "] 의 현재 인원 1 퇴장");
            }
        }

        return message;
//
//
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

    // Destination에서 ChatRoom ID 추출하는 메서드
    private Long parseChatRoomId(String destination) {
        // destination에서 chatRoomId를 추출하도록 구현
        // 예를 들어, "/topic/api/chat/room/123"에서 "123"을 추출
        // 실제 추출 방법은 애플리케이션의 destination 패턴에 따라 다를 수 있습니다.
        // 간단한 정규식 또는 문자열 처리를 사용하여 ChatRoom ID를 추출할 수 있습니다.

        // 예시 코드 (상황에 따라 수정이 필요)
        String[] parts = destination.split("/");
        if (parts.length > 0) {
            String lastPart = parts[parts.length - 1];
            try {
                return Long.parseLong(lastPart);
            } catch (NumberFormatException e) {
                // ChatRoom ID를 추출할 수 없는 경우
            }
        }

        return null;
    }
} // end class
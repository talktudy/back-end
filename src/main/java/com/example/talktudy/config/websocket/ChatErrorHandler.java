package com.example.talktudy.config.websocket;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ChatErrorHandler extends StompSubProtocolErrorHandler {

    public ChatErrorHandler() {
        super();
    }

    // 예외가 발생했을 시에 handleClientMessageProcessingError 실행
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex)
    {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(ex.getMessage());
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(ex.getMessage().getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());


                //handleErrorMessage(clientMessage, ex);
//
//        if (ex instanceof MalformedJwtException | ex instanceof SecurityException) {
//            return handleUnauthorizedException(clientMessage, ex);
//        } else if (ex instanceof ExpiredJwtException) {
//            log.info("Invalid JWT Token", ex);
//            return handleUnauthorizedException(clientMessage, ex);
//        } else if (ex instanceof UnsupportedJwtException) {
//            log.info("Invalid JWT Token", ex);
//            return handleUnauthorizedException(clientMessage, ex);
//        } else if (ex instanceof IllegalArgumentException) {
//            log.info("Invalid JWT Token", ex);
//            return handleUnauthorizedException(clientMessage, ex);
//        } else if (ex instanceof NullPointerException) {
//            log.info("Invalid JWT Token", ex);
//            return handleUnauthorizedException(clientMessage, ex);
//        }

        //return super.handleClientMessageProcessingError(clientMessage, ex);
    }

//    private Message<byte[]> handleErrorMessage(Message<byte[]> clientMessage, Throwable ex)
//    {
//        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
//        accessor.setMessage(ex.getMessage());
//        accessor.setLeaveMutable(true);
//
//        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8),
//    }
//
//    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage, ApiError apiError, String errorCode)
//    {
//
//        String message = apiError.getErrorMessage();
//
//        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
//
//        accessor.setMessage(errorCode);
//        accessor.setLeaveMutable(true);
//
//        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
//    }
} // end class

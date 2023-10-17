package com.example.talktudy.config.websocket;

import com.example.talktudy.filter.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ChatConfig implements WebSocketMessageBrokerConfigurer {
     private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 소켓 연결과 관련된 설정. ws://localhost:8080/ws/chat로 도착할때
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지를 구독하는 요청 url. 즉, 메세지를 받을 때
        registry.enableSimpleBroker("/sub");

        // 메세지를 발행하는 요청 url. 즉, 메세지를 보낼 떄
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
} // enc class

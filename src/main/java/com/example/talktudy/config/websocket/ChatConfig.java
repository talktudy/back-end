package com.example.talktudy.config.websocket;

import com.example.talktudy.filter.ChatPreHandler;
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
     private final ChatPreHandler chatPreHandler;
     private final ChatErrorHandler chatErrorHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 소켓 연결과 관련된 설정. ws://localhost:8080/ws/chat로 도착할때
        registry.addEndpoint("/chat").setAllowedOriginPatterns("*").withSockJS(); // .withSockJS();

        // 소켓 에러 핸들러 구현
        registry.setErrorHandler(chatErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메세지를 구독하는 요청 url. 즉, 메세지를 받을 때. 관습적으로 1:1은 queue, 1:N은 topic
        registry.enableSimpleBroker("/queue", "/topic");

        // 메세지를 발행하는 요청 url. 즉, 메세지를 보낼 떄. 메세지의 어떤 처리나 가공이 필요할때
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatPreHandler);
    }
} // enc class

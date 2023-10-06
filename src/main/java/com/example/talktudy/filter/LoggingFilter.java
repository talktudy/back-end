package com.example.talktudy.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String contentType = request.getHeader("Content-Type");

        log.info(method + uri + " 요청이 들어왔습니다.");
        log.info("Request URL : " + request.getRequestURL());
        log.info("Request userAgent : " + userAgent);
        log.info("Request contentType : " + contentType);

        // doFilter
        filterChain.doFilter(request, response);

        log.info("Response : " + method + uri + "가 상태 " + response.getStatus() + " 로 응답이 나갑니다.");
    }
}

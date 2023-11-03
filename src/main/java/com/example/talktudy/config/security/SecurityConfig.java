package com.example.talktudy.config.security;

import com.example.talktudy.filter.JwtAuthenticationFilter;
import com.example.talktudy.security.CustomAccessDeniedHandler;
import com.example.talktudy.security.CustomAuthenticationEntryPoint;
import com.example.talktudy.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .headers().frameOptions().sameOrigin()
                .and()
                .formLogin().disable()
                .csrf().disable() // 쿠키 기반이 아닌 JWT 기반이므로 사용하지 않음
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .httpBasic().disable() // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// Spring Security 세션 정책 : 세션을 생성 및 사용하지 않음
                .and()
                .authorizeRequests()// 조건별로 요청 허용/제한 설정
                .antMatchers("/api/auth/**", "/resources/static/**").permitAll() // 로그인, 회원가입 API는 permitAll()
                //.antMatchers("/post/create").hasRole("USER") // 유저만 가능한 엔드포인트 설
                //.antMatchers("/api/chat").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 적용
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:5500", "http://127.0.0.1:5173", "http://localhost:5173", "https://front-end-mu-lime.vercel.app"));
        configuration.setAllowCredentials(true); // token 주고 받을 때,
        configuration.addExposedHeader("Authorization"); // token
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

} // end class

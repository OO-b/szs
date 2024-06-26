package com.jobisnvillains.szs.common;

import com.jobisnvillains.szs.service.JwtService;
import com.jobisnvillains.szs.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService service;
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf((auth) -> auth.disable()); //csrf disable
        http.formLogin((auth) -> auth.disable()); //From 로그인 방식 disable
        http.httpBasic((auth) -> auth.disable()); //http basic 인증 방식 disable

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/szs/signup", "/szs/login", "/szs/scrap", "/szs/refund", "/swagger-ui/**", "/v3/api-docs/**", "/3o3/**").permitAll() // login, / /join 의 경로에서는 접근 허용
                        .anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthFilter(service, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers(
                "/swagger-resources/**",
                "/v3/api-docs/**",
                "/3o3/**");
    }

}

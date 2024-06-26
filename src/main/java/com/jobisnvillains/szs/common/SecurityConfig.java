package com.jobisnvillains.szs.common;

//import com.jobisnvillains.szs.jwt.LoginFilter;
import com.jobisnvillains.szs.service.JwtService;
import com.jobisnvillains.szs.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final JwtService jwtService;

//    private static final String[] AUTH_WHITELIST = {
//            "/szs/signup",
//            "/szs/login",
////            "/szs/scrap",
////            "/szs/refund",
//            "/swagger-ui/**",
//            "/v3/api-docs/**",
//            "/3o3/**"
//    };

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf((auth) -> auth.disable());
        //From 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());
        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(new JwtAuthFilter(jwtUtil, jwtService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}

package com.jobisnvillains.szs.common;

//import com.jobisnvillains.szs.jwt.LoginFilter;
import com.jobisnvillains.szs.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

//    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    //AuthenticationManager Bean 등록
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/szs/signup", "/szs/login", "/szs/scrap", "/szs/refund", "/swagger-ui/**", "/v3/api-docs/**", "/3o3/**").permitAll() // login, / /join 의 경로에서는 접근 허용
                        .anyRequest().authenticated());

//        http.authorizeRequests()
//                .antMatchers("/h2-console/**", "/szs/signup", "/swagger-ui.html").permitAll()
//                .and()
//                .csrf()
//                .ignoringAntMatchers("/h2-console/**", "/swagger-ui.html")
//                .disable()
//                .exceptionHandling()
//                .and()
//                .sessionManagement()
//                .authorizeRequests()
//
//        ;

//        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);


        //세션 설정
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}

package com.jobisnvillains.szs.common;

import com.jobisnvillains.szs.service.JwtService;
import com.jobisnvillains.szs.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService service;
    private final JWTUtil jwtUtil;
    private static final List<String> EXCLUDE_URLS = Arrays.asList("/szs/signup", "/szs/login", "/3o3/**");

    /**
     * JWT 토큰 검증 필터 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(shouldExclude(request)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader("Authorization");

            //JWT가 헤더에 있는 경우
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

                String token = authorizationHeader.split(" ")[1];

                //JWT 유효성 검증
                if (jwtUtil.validateToken(token)) {
                    filterChain.doFilter(request, response);
                } else {
                    makeErrorMessage("BAD_TOKEN", response);}

            } else {
                makeErrorMessage("BAD_REQUEST", response);
            }
        }
    }


    /**
     *  Filter 제외 할 페이지 확인
     *
     * @param request HttpServletRequest
     * @return boolean
     */
    private boolean shouldExclude(HttpServletRequest request) {
        return EXCLUDE_URLS.stream().anyMatch(url -> request.getRequestURI().contains(url));
    }

    /**
     *  에러메세지 생성
     *
     * @param errorType String
     * @param response HttpServletResponse
     * @return HttpServletResponse
     */
    private HttpServletResponse makeErrorMessage(String errorType, HttpServletResponse response) throws IOException {

        String msg = null;

        switch(errorType) {
            case "BAD_TOKEN" :
                msg = "잘못된 토큰 값입니다";
                break;
            case "BAD_REQUEST" :
                msg = "잘못된 접근입니다";
                break;
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("{\"error\": " + errorType + ", \"message\" : " + msg + "}");

        return response;
    }

}



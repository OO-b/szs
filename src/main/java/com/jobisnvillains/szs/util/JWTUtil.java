package com.jobisnvillains.szs.util;

import com.jobisnvillains.szs.domain.Member;
import com.jobisnvillains.szs.service.JwtService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey secretKey;
    private final JwtService jwtService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, JwtService jwtService) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.jwtService = jwtService;
    }

    public String getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", String.class);
    }

    /**
     *  토큰 생성
     *
     * @param userId String
     * @return String token
     */
    public String createJwt(String userId) {

        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1*(1000*60*60*24*365)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token);
            String userId = getUserId(token);
            Optional<Member> member = jwtService.loadUserByUsername(userId.toString());
            return member.isPresent();
        } catch (Exception e) {
            log.info("Invalid JWT Token", e);
        }
        return false;
    }

}

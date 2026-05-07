package com.example.helloserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-millis}")
    private long expirationMillis;

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // 生成 JWT
    public String generateToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(getSignKey())
                .compact();
    }

    // 解析完整 Claims
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 从 JWT 中提取用户名
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // 验证 JWT 是否过期
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // 验证 token 是否有效
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }
}

package com.saicharan.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Key signingKey;
    private final long tokenValidity;

    public JwtUtil(@Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long validity) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidity = validity;
    }

    public String generateToken(String username, String role) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + tokenValidity);

        return Jwts.builder()
                .setSubject(username)
                .addClaims(Map.of("role", role))
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    public String getRole(String token) {
        Object role = parseToken(token).getBody().get("role");
        return role != null ? role.toString() : null;
    }

    public boolean validate(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
}

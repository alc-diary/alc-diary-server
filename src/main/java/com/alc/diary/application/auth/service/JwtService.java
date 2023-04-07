package com.alc.diary.application.auth.service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    private static final long TOKEN_VALID_PERIOD_MILLI = 60 * 60 * 1000L;

    @Value("${server.secret}")
    private static final String serverSecret = "SERVER_SECRET";

    private static Instant currentTime = Instant.now();

    public String generateToken(long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(currentTime))
                .setExpiration(Date.from(currentTime.plusMillis(TOKEN_VALID_PERIOD_MILLI)))
                .signWith(SignatureAlgorithm.HS256, serverSecret)
                .compact();
    }

    public long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Integer.parseInt(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return !expiration.before(Date.from(currentTime));
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(serverSecret)
                .setClock(() -> Date.from(currentTime))
                .parseClaimsJws(token)
                .getBody();
    }
}

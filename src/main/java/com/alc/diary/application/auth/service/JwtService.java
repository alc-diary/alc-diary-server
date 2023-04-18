package com.alc.diary.application.auth.service;

import com.alc.diary.domain.auth.error.AuthError;
import com.alc.diary.domain.exception.DomainException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private static final long TOKEN_VALID_PERIOD_MILLI = 60 * 60 * 1000L;
    private final String serverSecret;
    private final Clock systemClock;

    public JwtService(@Value("${server.secret}") String serverSecret, Clock systemClock) {
        this.serverSecret = serverSecret;
        this.systemClock = systemClock;
    }

    public String generateToken(long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(systemClock.instant()))
                .setExpiration(Date.from(systemClock.instant().plusMillis(TOKEN_VALID_PERIOD_MILLI)))
                .signWith(SignatureAlgorithm.HS256, serverSecret)
                .compact();
    }

    public long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return !expiration.before(Date.from(systemClock.instant()));
        } catch (ExpiredJwtException e) {
            throw new DomainException(AuthError.EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new DomainException(AuthError.INVALID_ACCESS_TOKEN);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(serverSecret)
                .setClock(() -> Date.from(systemClock.instant()))
                .parseClaimsJws(token)
                .getBody();
    }
}

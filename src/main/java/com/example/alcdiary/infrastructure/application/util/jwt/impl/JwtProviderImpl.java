package com.example.alcdiary.infrastructure.application.util.jwt.impl;

import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import com.example.alcdiary.domain.model.user.UserIdModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
class JwtProviderImpl implements JwtProvider {

    private final Environment env;
    private static long TOKEN_VALID_PERIOD_MILLI = 30 * 24 * 60 * 60 * 1000L;

    @Override
    public String generateToken(UserIdModel userIdModel) {
        Claims claims = Jwts.claims().setId(userIdModel.parse());
        Date now = new Date();
        System.out.println(env.getProperty("server.secret"));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_PERIOD_MILLI))
                .signWith(SignatureAlgorithm.HS256, env.getProperty("server.secret"))
                .compact();
    }

    @Override
    public boolean validateToken(String bearerToken) {
        Jws<Claims> claims = getClaims(bearerToken);
        return claims
                .getBody()
                .getExpiration()
                .after(new Date());
    }

    @Override
    public UserIdModel getKey(String bearerToken) {
        Jws<Claims> claims = getClaims(bearerToken);
        return UserIdModel.from(claims.getBody().getId());
    }

    private Jws<Claims> getClaims(String bearerToken) {
        if (!validBearerToken(bearerToken)) {
            throw new AlcException(AuthError.INVALID_ACCESS_TOKEN);
        }
        String refreshToken = getPureToken(bearerToken);
        try {
            return Jwts.parser()
                    .setSigningKey(env.getProperty("server.secret"))
                    .parseClaimsJws(refreshToken);
        } catch (Exception e) {
            throw new AlcException(AuthError.INVALID_ACCESS_TOKEN);
        }
    }

    private boolean validBearerToken(String bearerToken) {
        return bearerToken.startsWith("Bearer ");
    }

    private String getPureToken(String bearerToken) {
        return bearerToken.substring("Bearer ".length());
    }
}

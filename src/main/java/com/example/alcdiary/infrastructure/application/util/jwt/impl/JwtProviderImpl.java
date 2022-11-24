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
    private static long TOKEN_VALID_PERIOD_MILLI = 5 * 60 * 1000L;

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
    public boolean validateToken(String token) {
        Jws<Claims> claims = getClaims(token);
        return claims
                .getBody()
                .getExpiration()
                .after(new Date());
    }

    @Override
    public UserIdModel getKey(String token) {
        Jws<Claims> claims = getClaims(token);
        return UserIdModel.from(claims.getBody().getId());
    }

    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(env.getProperty("server.secret"))
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new AlcException(AuthError.INVALID_ACCESS_TOKEN);
        }
    }
}

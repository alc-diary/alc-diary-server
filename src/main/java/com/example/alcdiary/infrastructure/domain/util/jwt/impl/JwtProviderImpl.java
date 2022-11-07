package com.example.alcdiary.infrastructure.domain.util.jwt.impl;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.util.jwt.JwtProvider;
import com.example.alcdiary.infrastructure.entity.RefreshToken;
import com.example.alcdiary.infrastructure.jpa.RefreshTokenJpaRepository;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProviderImpl implements JwtProvider {

    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private String secretKey = "secretKey";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Override
    public String createToken(UserModel userModel) {
        LocalDateTime now = LocalDateTime.now();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("alc-server")
                .setIssuedAt(new Date())
                .claim("userId", userModel.getId())
                .claim("issuedAt", now)
                .claim("expiredAt", now.plusMinutes(5))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public UserModel resolveToken(String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader);
        String token = extractToken(authorizationHeader);

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        LocalDateTime expiredAt = (LocalDateTime) claims.get("expiredAt");
        String userId = (String) claims.get("userId");

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new AlcException(AuthError.EXPIRED_ACCESS_TOKEN);
        }
        RefreshToken refreshToken = refreshTokenJpaRepository.findByUserId(userId);
        refreshToken.updateExpiredAt();

        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new AlcException(UserError.NOT_FOUND_USER))
                .convertToDomainModel();
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AlcException(AuthError.INVALID_AUTHORIZATION_HEADER);
        }
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }
}

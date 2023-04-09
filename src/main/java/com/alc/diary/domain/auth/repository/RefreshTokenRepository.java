package com.alc.diary.domain.auth.repository;

import com.alc.diary.domain.auth.RefreshToken;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RefreshTokenRepository extends Repository<RefreshToken, Long> {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);
}

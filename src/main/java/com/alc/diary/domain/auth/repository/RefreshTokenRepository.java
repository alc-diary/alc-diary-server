package com.alc.diary.domain.auth.repository;

import com.alc.diary.domain.auth.RefreshToken;
import org.springframework.data.repository.Repository;

public interface RefreshTokenRepository extends Repository<RefreshToken, Long> {

    RefreshToken save(RefreshToken refreshToken);
}

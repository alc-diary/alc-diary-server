package com.alc.diary.domain.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByUserId(long userId);

    void deleteByUserId(long userId);

    Optional<FcmToken> findByToken(String token);
}

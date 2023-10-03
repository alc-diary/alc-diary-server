package com.alc.diary.domain.auth;

import com.alc.diary.domain.auth.policy.TokenExpiredPolicy;
import com.alc.diary.domain.auth.policy.TokenGeneratePolicy;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        User mockUser = Mockito.mock(User.class);
        Clock mockClock = Clock.systemDefaultZone();
        TokenGeneratePolicy mockTokenGeneratePolicy = () -> UUID.randomUUID().toString();
        TokenExpiredPolicy mockTokenExpiredPolicy = clock -> LocalDateTime.now(clock).plusDays(1);

        refreshToken = new RefreshToken(
                mockUser,
                mockClock,
                mockTokenGeneratePolicy,
                mockTokenExpiredPolicy
        );
    }

    @DisplayName("RefreshToken이 만료되었으면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenRefreshTokenExpired() {
        // when
        refreshToken.expire();

        // then
        assertThat(refreshToken.isExpired()).isTrue();
    }

    @DisplayName("RefreshToken이 만료되지 않았으면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenRefreshTokenNotExpired() {
        // then
        assertThat(refreshToken.isExpired()).isFalse();
    }

    @Test
    void shouldReturnTrueWhenTimeIsAfterExpiredAt() {
        // given
        Clock mockClock = Clock.fixed(
                LocalDateTime.of(2021, 1, 1, 0, 0).toInstant(ZoneOffset.UTC),
                ZoneId.of("UTC")
        );

        TokenExpiredPolicy mockTokenExpiredPolicy = clock -> LocalDateTime.now(clock).minusDays(1);

        // when
        refreshToken = new RefreshToken(
                Mockito.mock(User.class),
                mockClock,
                () -> UUID.randomUUID().toString(),
                mockTokenExpiredPolicy
        );

        // then
        assertThat(refreshToken.isExpired()).isTrue();
    }
}
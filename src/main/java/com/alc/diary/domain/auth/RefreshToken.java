package com.alc.diary.domain.auth;

import com.alc.diary.domain.auth.policy.DefaultExpiredPolicy;
import com.alc.diary.domain.auth.policy.TokenExpiredPolicy;
import com.alc.diary.domain.auth.policy.TokenGeneratePolicy;
import com.alc.diary.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Table(name = "refresh_tokens")
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "token", length = 40, nullable = false, updatable = false)
    private String token;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false, updatable = false)
    private LocalDateTime expiredAt;

    @Getter(AccessLevel.NONE)
    @Column(name = "is_expired", nullable = false)
    private Boolean isExpired;

    public RefreshToken(
        User user,
        Clock currentTime,
        TokenGeneratePolicy tokenGeneratePolicy,
        TokenExpiredPolicy tokenExpiredPolicy
    ) {
        this.user = user;
        this.token = tokenGeneratePolicy.generate();
        this.createdAt = LocalDateTime.now(currentTime);
        this.expiredAt = tokenExpiredPolicy.calculate(currentTime);
        this.isExpired = false;
    }

    public RefreshToken() {
    }

    public static RefreshToken getDefault(User user) {
        return new RefreshToken(
            user,
            Clock.systemDefaultZone(),
            () -> UUID.randomUUID().toString(),
            new DefaultExpiredPolicy()
        );
    }

    public void expire() {
        this.isExpired = true;
    }

    public boolean isExpired() {
        return isExpired || expiredAt.isBefore(LocalDateTime.now());
    }
}
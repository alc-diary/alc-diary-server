package com.alc.diary.domain.auth;

import com.alc.diary.domain.auth.policy.TokenExpiredPolicy;
import com.alc.diary.domain.auth.policy.TokenGeneratePolicy;
import com.alc.diary.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Table(name = "refresh_tokens")
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", length = 40)
    private String token;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false, updatable = false)
    private LocalDateTime expiredAt;

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
    }

    public RefreshToken() {
    }
}
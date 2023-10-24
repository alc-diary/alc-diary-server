package com.alc.diary.domain.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fcm_tokens")
@Entity
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private Long userId;

    @Column(name = "token", length = 255, nullable = false, unique = true)
    private String token;

    private FcmToken(Long id, Long userId, String token) {
        validInput(id, userId, token);

        this.id = id;
        this.userId = userId;
        this.token = token;
    }

    private void validInput(Long id, Long userId, String token) {
        if (userId == null) {
            throw new IllegalArgumentException();
        }
        if (token == null) {
            throw new IllegalArgumentException();
        }
    }

    public static FcmToken create(long userId, String token) {
        return new FcmToken(null, userId, token);
    }

    public void updateToken(String newToken) {
        if (StringUtils.isBlank(newToken)) {
            throw new IllegalArgumentException();
        }
        token = newToken;
    }
}

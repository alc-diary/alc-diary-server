package com.alc.diary.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification_settings")
@Entity
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private long userId;

    @Column(name = "notification_enabled", nullable = false)
    private Boolean notificationEnabled;

    private NotificationSetting(Long id, Long userId, Boolean notificationEnabled) {
        this.id = id;
        this.userId = userId;
        this.notificationEnabled = notificationEnabled;
    }

    public static NotificationSetting create(long userId) {
        return new NotificationSetting(null, userId, true);
    }

    public void enableNotification() {
        this.notificationEnabled = true;
    }

    public void disableNotification() {
        this.notificationEnabled = false;
    }
}

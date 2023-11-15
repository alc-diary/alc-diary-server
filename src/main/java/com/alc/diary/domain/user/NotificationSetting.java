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

    @Column(name = "friend_request", nullable = false)
    private Boolean friendRequest;

    @Column(name = "calendar_tag", nullable = false)
    private Boolean calendarTag;

    private NotificationSetting(Long id, Long userId, Boolean friendRequest, Boolean calendarTag) {
        this.id = id;
        this.userId = userId;
        this.friendRequest = friendRequest;
        this.calendarTag = calendarTag;
    }

    public static NotificationSetting create(long userId) {
        return new NotificationSetting(null, userId, true, true);
    }

    public void updateFriendRequest(boolean newFriendRequest) {
        friendRequest = newFriendRequest;
    }

    public void updateCalendarTag(boolean newCalendarTag) {
        calendarTag = newCalendarTag;
    }
}

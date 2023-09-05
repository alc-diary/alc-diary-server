package com.alc.diary.domain.calendar;

import com.alc.diary.domain.calendar.error.PhotoError;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendar_photos")
@Entity
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Column(name = "url", length = 1000, nullable = false)
    private String url;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Photo(long userId, String url, LocalDateTime deletedAt) {
        this.userId = userId;
        this.url = url;
        this.deletedAt = deletedAt;
    }

    public static Photo create(long userId, String url) {
        return new Photo(userId, url, null);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isOwner(long userId) {
        return this.userId == userId;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}

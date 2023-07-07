package com.alc.diary.domain.calendar;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendar_comments")
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Column(name = "text", length = 500, nullable = false)
    private String text;

    @Column(name = "user_visible_created_at", nullable = false)
    public ZonedDateTime userVisibleCreatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private Comment(long userId, String text, LocalDateTime deletedAt) {
        this.userId = userId;
        this.text = text;
        this.deletedAt = deletedAt;
    }

    public static Comment create(long userId, String text) {
        return new Comment(userId, text, null);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}

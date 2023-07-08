package com.alc.diary.domain.calendar;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
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

    private Photo(long userId, String url) {
        this.userId = userId;
        this.url = url;
    }

    public static Photo create(long userId, String url) {
        return new Photo(userId, url);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}

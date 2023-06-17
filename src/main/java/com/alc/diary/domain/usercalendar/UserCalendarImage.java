package com.alc.diary.domain.usercalendar;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendar_images")
@Entity
public class UserCalendarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_calendar_id")
    private UserCalendar userCalendar;

    @Column(name = "image_url", length = 1000, nullable = false)
    private String imageUrl;

    public UserCalendarImage(UserCalendar userCalendar, String imageUrl) {
        this.userCalendar = userCalendar;
        userCalendar.addImages(List.of(this));
        this.imageUrl = imageUrl;
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }
}

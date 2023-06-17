package com.alc.diary.domain.usercalendar;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendars")
@Entity
public class UserCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "condition", length = 20)
    private String condition;

    @OneToMany(mappedBy = "userCalendar", cascade = CascadeType.PERSIST)
    private List<UserCalendarImage> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private UserCalendarStatus userCalendarStatus;

    public UserCalendar(
            User user,
            Calendar calendar,
            String content,
            String condition,
            UserCalendarStatus userCalendarStatus
    ) {
        if (user == null) {
            throw new RuntimeException();
        }
        if (calendar == null) {
            throw new RuntimeException();
        }
        if (userCalendarStatus == null) {
            throw new RuntimeException();
        }
        this.user = user;
        this.calendar = calendar;
        this.content = content;
        this.condition = condition;
        this.userCalendarStatus = userCalendarStatus;
    }

    public static UserCalendar createRequest(
            User user,
            Calendar calendar
    ) {
        return new UserCalendar(user, calendar, null, null, UserCalendarStatus.PENDING);
    }

    public void addImages(Iterable<UserCalendarImage> images) {
        for (UserCalendarImage image : images) {
            this.images.add(image);
            image.setUserCalendar(this);
        }
    }
}

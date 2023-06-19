package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.usercalendar.UserCalendar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendars")
@Entity
public class Calendar extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private Set<UserCalendar> userCalendars = new HashSet<>();

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "drink_start_time", nullable = false)
    private LocalDateTime drinkStartTime;

    @Column(name = "drink_end_time", nullable = false)
    private LocalDateTime drinkEndTime;

    public Calendar(User owner, String title, LocalDateTime drinkStartTime, LocalDateTime drinkEndTime) {
        if (owner == null) {
            throw new RuntimeException();
        }
        if (title == null) {
            throw new RuntimeException();
        }
        this.owner = owner;
        this.title = title;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
    }

    public void addUserCalendar(UserCalendar userCalendar) {
        if (userCalendar == null) {
            throw new RuntimeException();
        }
        userCalendars.add(userCalendar);
        userCalendar.setCalendar(this);
    }

    public void addUserCalendars(List<UserCalendar> userCalendars) {
        for (UserCalendar userCalendar : userCalendars) {
            this.userCalendars.add(userCalendar);
            userCalendar.setCalendar(this);
        }
    }

    public LocalDate getLocalDate() {
        return drinkStartTime.toLocalDate();
    }
}

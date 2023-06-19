package com.alc.diary.domain.usercalendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.drink.UserCalendarDrink;
import com.alc.diary.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString(exclude = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendars")
@Entity
public class UserCalendar extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @OneToMany(mappedBy = "userCalendar", cascade = CascadeType.PERSIST)
    private Set<UserCalendarDrink> drinks = new HashSet<>();

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "condition", length = 20)
    private String condition;

    @OneToMany(mappedBy = "userCalendar", cascade = CascadeType.PERSIST)
    private Set<UserCalendarImage> images = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private UserCalendarStatus status;

    public UserCalendar(
            User user,
            String content,
            String condition,
            UserCalendarStatus status
    ) {
        if (user == null) {
            throw new RuntimeException();
        }
        if (status == null) {
            throw new RuntimeException();
        }
        this.user = user;
        this.content = content;
        this.condition = condition;
        this.status = status;
    }

    public static UserCalendar createUserCalendarRequest(User user) {
        return new UserCalendar(user, null, null, UserCalendarStatus.PENDING);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void addImages(Iterable<UserCalendarImage> images) {
        for (UserCalendarImage image : images) {
            this.images.add(image);
            image.setUserCalendar(this);
        }
    }

    public void addDrinks(Iterable<UserCalendarDrink> drinks) {
        for (UserCalendarDrink drink : drinks) {
            this.drinks.add(drink);
            drink.setUserCalendar(this);
        }
    }
}

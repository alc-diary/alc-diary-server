package com.alc.diary.domain.usercalendar;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendars")
@Entity
public class UserCalendar extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Audited
    @Column(name = "calendar_id", nullable = false)
    private long calendarId;

    @Audited
    @Column(name = "content", length = 1000)
    private String content;

    @Audited
    @Column(name = "`condition`", length = 20)
    private String condition;

    @OneToMany(mappedBy = "userCalendar", cascade = CascadeType.PERSIST)
    private List<UserCalendarDrink> drinks = new ArrayList<>();

    @OneToMany(mappedBy = "userCalendar", cascade = CascadeType.PERSIST)
    private List<UserCalendarImage> images = new ArrayList<>();

    @Audited
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Audited
    @Column(name = "total_calories", nullable = false)
    private int totalCalories;

    @Audited
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    private UserCalendar(
            Long userId,
            Long calendarId,
            String content,
            String condition,
            int totalPrice,
            int totalCalories,
            boolean isDeleted
    ) {
        if (userId == null) {

        }
        if (calendarId == null) {

        }
        if (StringUtils.length(content) > 1000) {

        }
        this.userId = userId;
        this.calendarId = calendarId;
        this.content = content;
        this.condition = condition;
        this.totalPrice = totalPrice;
        this.totalCalories = totalCalories;
        this.isDeleted = isDeleted;
    }

    public void addImages(Iterable<UserCalendarImage> images) {
        for (UserCalendarImage image : images) {
            this.images.add(image);
            image.setUserCalendar(this);
        }
    }

//    public void addDrinks(Iterable<UserCalendarDrink> drinks) {
//        for (UserCalendarDrink drink : drinks) {
//            this.drinks.add(drink);
//            drink.setUserCalendar(this);
//        }
//    }
}

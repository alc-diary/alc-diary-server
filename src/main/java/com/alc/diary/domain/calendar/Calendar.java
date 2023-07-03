package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "calendars",
        indexes = {@Index(name = "idx_calendars_drink_start_time", columnList = "drink_start_time")}
)
@Entity
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "owner_id", nullable = false) // Foreign key referencing 'users' table by 'id' column
    private long ownerId;

    @Audited
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Audited
    @Column(name = "drink_start_time", nullable = false)
    private ZonedDateTime drinkStartTime;

    @Audited
    @Column(name = "drink_end_time", nullable = false)
    private ZonedDateTime drinkEndTime;

    @OneToMany(mappedBy = "calendar", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserCalendar> userCalendars = new ArrayList<>();

    private Calendar(Long ownerId, String title, ZonedDateTime drinkStartTime, ZonedDateTime drinkEndTime) {
        if (ownerId == null) {
            throw new DomainException(CalendarError.OWNER_NULL);
        }
        if (title == null) {
            throw new DomainException(CalendarError.TITLE_NULL);
        }
        if (drinkStartTime == null) {
            throw new DomainException(CalendarError.DRINK_START_TIME_NULL);
        }
        if (drinkEndTime == null) {
            throw new DomainException(CalendarError.DRINK_END_TIME_NULL);
        }

        this.ownerId = ownerId;
        this.title = title;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
    }

    public static Calendar create(
            Long ownerId,
            String title,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime
    ) {
        return new Calendar(
                ownerId,
                title,
                drinkStartTime,
                drinkEndTime
        );
    }

    public void addUserCalendars(Iterable<UserCalendar> userCalendars) {
        for (UserCalendar userCalendar : userCalendars) {
            addUserCalendar(userCalendar);
        }
    }

    public void addUserCalendar(UserCalendar userCalendar) {
        userCalendars.add(userCalendar);
        userCalendar.setCalendar(this);
    }

    public boolean isInvolvedUser(long userId) {
        return userCalendars.stream()
                .anyMatch(userCalendar -> userCalendar.isOwner(userId));
    }

    public Optional<UserCalendar> getUserCalendarByUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.isOwner(userId))
                .findFirst();
    }

    public List<UserCalendar> getUserCalendarsExceptByUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> !userCalendar.isOwner(userId))
                .toList();
    }

    public LocalDate getDate() {
        return drinkStartTime.toLocalDate();
    }

    public DayOfWeek getDayOfWeek() {
        return drinkStartTime.getDayOfWeek();
    }

    public void updateTitle(long userId, String newTitle) {
        if (!isOwner(userId)) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
        this.title = newTitle;
    }

    public void updateDrinkStartTime(long userId, ZonedDateTime newDrinkStartTime) {
        if (!isOwner(userId)) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
        drinkStartTime = newDrinkStartTime;
    }

    public void updateDrinkEndTime(long userId, ZonedDateTime newDrinkEndTime) {
        if (!isOwner(userId)) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
        drinkEndTime = newDrinkEndTime;
    }

    public void updateContent(long userId, String newContent) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId).orElseThrow();
        userCalendar.updateContent(userId, newContent);
    }

    public void updateCondition(long userId, String newCondition) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId).orElseThrow();
        userCalendar.updateCondition(userId, newCondition);
    }

    public boolean isOwner(long userId) {
        return ownerId == userId;
    }

    public void updateUserCalendarDrink(long userCalendarId, long userCalendarDrinkId, float newQuantity) {
        UserCalendar foundUserCalendar = userCalendars.stream()
                .filter(userCalendar -> userCalendar.getId() == userCalendarId)
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        foundUserCalendar.updateDrinkById(userCalendarDrinkId, newQuantity);
    }

    public void removeDrinkByIds(long userId, List<Long> userCalendarDrinkIds) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId).orElseThrow();
        userCalendar.removeDrinksByIds(userCalendarDrinkIds);
    }

    public void removeImagesByIds(long userId, List<Long> userCalendarImageIds) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId).orElseThrow(RuntimeException::new);
        userCalendar.removeImagesByIds(userCalendarImageIds);
    }
}

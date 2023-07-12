package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.vo.DrinkRecordUpdateVo;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendars")
@Entity
public class Calendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "owner_id")
    private long ownerId;

    @Audited
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Audited
    @Column(name = "total_drink_quantity", nullable = false)
    private float totalDrinkQuantity;

    @Audited
    @Column(name = "drink_start_time", nullable = false)
    private ZonedDateTime drinkStartTime;

    @Audited
    @Column(name = "drink_end_time", nullable = false)
    private ZonedDateTime drinkEndTime;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private List<UserCalendar> userCalendars = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    private Calendar(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime,
            LocalDateTime deletedAt,
            ZonedDateTime now
    ) {
        if (title == null) {
            throw new DomainException(CalendarError.NULL_TITLE);
        }
        if (StringUtils.length(title) > 100) {
            throw new DomainException(CalendarError.TITLE_LENGTH_EXCEEDED);
        }
        if (drinkStartTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_START_TIME);
        }
        if (drinkEndTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_END_TIME);
        }
        if (drinkStartTime.isAfter(drinkEndTime)) {
            throw new DomainException(CalendarError.START_TIME_AFTER_END_TIME);
        }
        if (drinkEndTime.isAfter(now)) {
            throw new DomainException(CalendarError.END_TIME_IN_FUTURE);
        }
        this.ownerId = ownerId;
        this.title = title;
        this.totalDrinkQuantity = totalDrinkQuantity;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
        this.deletedAt = deletedAt;
    }

    public static Calendar create(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime
    ) {
        return new Calendar(
                ownerId,
                title,
                totalDrinkQuantity,
                drinkStartTime,
                drinkEndTime,
                null,
                ZonedDateTime.now()
        );
    }

    static Calendar create(
            long ownerId,
            String title,
            float totalDrinkQuantity,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime,
            ZonedDateTime now
    ) {
        return new Calendar(ownerId, title, totalDrinkQuantity, drinkStartTime, drinkEndTime, null, now);
    }

    public void addUserCalendars(Collection<UserCalendar> userCalendarEntries) {
        for (UserCalendar userCalendar : userCalendarEntries) {
            addUserCalendar(userCalendar);
        }
    }

    public void addUserCalendar(UserCalendar userCalendar) {
        userCalendars.add(userCalendar);
        userCalendar.setCalendar(this);
    }

    public void addPhotos(List<Photo> photos) {
        for (Photo photo : photos) {
            addPhoto(photo);
        }
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setCalendar(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCalendar(this);
    }

    public void addDrinkRecords(long userId, Collection<DrinkRecord> drinkRecords) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.addDrinkRecords(drinkRecords);
    }

    public boolean isOwner(long userId) {
        return ownerId == userId;
    }

    public boolean hasPermission(long userId) {
        return userCalendars.stream()
                .anyMatch(userCalendar -> userCalendar.isOwner(userId));
    }

    public Optional<UserCalendar> findUserCalendarByUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.isOwner(userId))
                .findFirst();
    }

    public List<UserCalendar> findUserCalendarsExcludingUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> !userCalendar.isOwner(userId))
                .toList();
    }

    public void updateTitle(long userId, String newTitle) {
        if (newTitle == null) {
            throw new DomainException(CalendarError.NULL_TITLE);
        }
        if (StringUtils.length(newTitle) > 100) {
            throw new DomainException(CalendarError.TITLE_LENGTH_EXCEEDED);
        }
        if (ownerId != userId) {
            throw new DomainException(CalendarError.NO_PERMISSION_TO_UPDATE_TITLE);
        }
        title = newTitle;
    }

    public void updateContent(long userId, String newContent) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.updateContent(userId, newContent);
    }

    public void updateCondition(long userId, String newCondition) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.updateCondition(userId, newCondition);
    }

    public void updateDrinkStartTimeAndEndTime(
            long userId,
            ZonedDateTime newDrinkStartTime,
            ZonedDateTime newDrinkEndTime
    ) {
        if (ownerId != userId) {
            throw new DomainException(CalendarError.NO_PERMISSION);
        }
        if (newDrinkStartTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_START_TIME);
        }
        if (newDrinkEndTime == null) {
            throw new DomainException(CalendarError.NULL_DRINK_END_TIME);
        }
        if (newDrinkStartTime.isAfter(newDrinkEndTime)) {
            throw new DomainException(CalendarError.START_TIME_AFTER_END_TIME);
        }
        drinkStartTime = newDrinkStartTime;
        drinkEndTime = newDrinkEndTime;
    }

    public void updateDrinkRecords(long userId, List<DrinkRecordUpdateVo> updateVo) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.updateDrinkRecord(userId, updateVo);
    }

    public void deleteDrinkRecords(long userId, List<Long> drinkRecordIdsToDelete) {
        UserCalendar userCalendar = getUserCalendarByUserId(userId);
        userCalendar.deleteDrinkRecords(userId, drinkRecordIdsToDelete);
    }

    private UserCalendar getUserCalendarByUserId(long userId) {
        return userCalendars.stream()
                .filter(userCalendar -> userCalendar.isOwner(userId))
                .findFirst()
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
    }

    public LocalDate getDrinkStartTimeLocalDate() {
        return getDrinkStartTimeLocalDate(ZoneId.of("Asia/Seoul"));
    }

    public LocalDate getDrinkStartTimeLocalDate(ZoneId zoneId) {
        return drinkStartTime.withZoneSameInstant(zoneId).toLocalDate();
    }

    public float calculateTotalDrinkQuantity() {
        return (float) userCalendars.stream()
                .flatMap(userCalendar -> userCalendar.getDrinkRecords().stream())
                .mapToDouble(DrinkRecord::getQuantity)
                .sum();
    }

    public DrinkType getMostConsumedDrinkType() {
        return userCalendars.stream()
                .flatMap(userCalendar -> userCalendar.getDrinkRecords().stream())
                .collect(Collectors.groupingBy(
                        DrinkRecord::getType,
                        Collectors.summingDouble(DrinkRecord::getQuantity)
                )).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public int getTotalPrice() {
        return userCalendars.stream()
                .mapToInt(UserCalendar::totalPrice)
                .sum();
    }

    public int getTotalCalories() {
        return userCalendars.stream()
                .mapToInt(UserCalendar::totalCalories)
                .sum();
    }

    public void deleteUserCalendar(long userId, long userCalendarId) {
        UserCalendar foundUserCalendar = userCalendars.stream()
                .filter(userCalendar -> userCalendar.getId() == userCalendarId)
                .findFirst()
                .orElseThrow(() -> new DomainException(UserCalendarError.USER_CALENDAR_NOT_FOUND));
        foundUserCalendar.delete(userId);

        userCalendars.stream()
                .filter(userCalendar -> !userCalendar.isOwner(userId))
                .findFirst()
                .ifPresentOrElse(
                        userCalendar -> ownerId = userCalendar.getUserId(),
                        () -> deletedAt = LocalDateTime.now()
                );
    }
}

package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.error.CalendarError;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
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
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime,
            LocalDateTime deletedAt
    ) {
        this.ownerId = ownerId;
        this.title = title;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
        this.deletedAt = deletedAt;
    }

    public static Calendar create(
            long ownerId,
            String title,
            ZonedDateTime drinkStartTime,
            ZonedDateTime drinkEndTime
    ) {
        if (title == null) {
            throw new DomainException(CalendarError.NULL_TITLE);
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
        if (drinkEndTime.isAfter(ZonedDateTime.now())) {
            throw new DomainException(CalendarError.END_TIME_IN_FUTURE);
        }
        return new Calendar(ownerId, title, drinkStartTime, drinkEndTime, null);
    }

    public void addUserCalendars(Iterable<UserCalendar> userCalendarEntries) {
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
}

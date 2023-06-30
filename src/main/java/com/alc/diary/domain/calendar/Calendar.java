package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.usercalendar.UserCalendar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "owner_id", nullable = false)
    private long ownerId;

    @Audited
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Audited
    @Column(name = "drink_start_time", nullable = false)
    private LocalDateTime drinkStartTime;

    @Audited
    @Column(name = "drink_end_time", nullable = false)
    private LocalDateTime drinkEndTime;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.PERSIST)
    private List<UserCalendar> userCalendars = new ArrayList<>();

    private Calendar(Long ownerId, String title, LocalDateTime drinkStartTime, LocalDateTime drinkEndTime) {
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
            LocalDateTime drinkStartTime,
            LocalDateTime drinkEndTime
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

    public boolean isUserInvolved(long userId) {
        return userCalendars.stream()
                .anyMatch(userCalendar -> userCalendar.isOwner(userId));
    }
}

package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Audited
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Audited
    @Column(name = "drink_start_time", nullable = false)
    private LocalDateTime drinkStartTime;

    @Audited
    @Column(name = "drink_end_time", nullable = false)
    private LocalDateTime drinkEndTime;

    public Calendar(User owner, String title, LocalDateTime drinkStartTime, LocalDateTime drinkEndTime) {
        if (owner == null) {
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

        this.owner = owner;
        this.title = title;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
    }

    public LocalDate getLocalDate() {
        return drinkStartTime.toLocalDate();
    }
}

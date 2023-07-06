package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendar_entry")
@Entity
public class CalendarEntry extends BaseEntity {

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
    @Column(name = "content", length = 1000)
    private String content;

    @Audited
    @Column(name = "drink_start_time", nullable = false)
    private ZonedDateTime drinkStartTime;

    @Audited
    @Column(name = "drink_end_time", nullable = false)
    private ZonedDateTime drinkEndTime;

    @OneToMany(mappedBy = "calendarEntry")
    private List<UserCalendarEntry> userCalendarEntries;

    @OneToMany(mappedBy = "calendarEntry")
    private List<Photo> photos;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
}

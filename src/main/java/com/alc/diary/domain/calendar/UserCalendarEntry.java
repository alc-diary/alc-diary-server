package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendar_entry")
@Entity
public class UserCalendarEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Audited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_entry_id")
    private CalendarEntry calendarEntry;

    @Audited
    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "userCalendarEntry")
    private List<DrinkRecord> drinks = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

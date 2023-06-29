package com.alc.diary.domain.usercalendar;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_calendar_drinks",
        indexes = {@Index(name = "idx_user_calendar_drinks_user_calendar_id", columnList = "user_calendar_id")}
)
@Entity
public class UserCalendarDrink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_calendar_id", foreignKey = @ForeignKey(name = "fk_user_calendar_drinks_user_calendars"))
    private UserCalendar userCalendar;

    @Column(name = "drink_id", nullable = false)
    private long drinkId;

    @Column(name = "quantity", nullable = false)
    private float quantity;
}

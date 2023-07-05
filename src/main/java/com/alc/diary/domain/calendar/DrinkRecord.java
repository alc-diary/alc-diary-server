package com.alc.diary.domain.calendar;

import com.alc.diary.domain.drink.DrinkType;
import com.alc.diary.domain.drink.DrinkUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "drink_record")
@Entity
public class DrinkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_calendar_entry_id")
    private UserCalendarEntry userCalendarEntry;

    @Audited
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DrinkType type;

    @Audited
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private DrinkUnit unit;

    @Audited
    @Column(name = "quantity", nullable = false)
    private float quantity;

    @Audited
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

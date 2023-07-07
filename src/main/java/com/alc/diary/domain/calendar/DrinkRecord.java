package com.alc.diary.domain.calendar;

import com.alc.diary.domain.calendar.error.DrinkRecordError;
import com.alc.diary.domain.drink.DrinkType;
import com.alc.diary.domain.drink.DrinkUnit;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(exclude = {"userCalendar"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendar_drink_records")
@Entity
public class DrinkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_calendar_id")
    private UserCalendar userCalendar;

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

    private DrinkRecord(DrinkType drinkType, DrinkUnit drinkUnit, float quantity, LocalDateTime deletedAt) {
        if (drinkType == null) {
            throw new DomainException(DrinkRecordError.NULL_DRINK_TYPE);
        }
        if (drinkUnit == null) {
            throw new DomainException(DrinkRecordError.NULL_DRINK_UNIT);
        }
        if (!drinkType.isUnitAllowed(drinkUnit)) {
            throw new DomainException(DrinkRecordError.INVALID_DRINK_UNIT);
        }
        if (quantity == 0) {
            throw new DomainException(DrinkRecordError.ZERO_QUANTITY);
        }
        this.type = drinkType;
        this.unit = drinkUnit;
        this.quantity = quantity;
        this.deletedAt = deletedAt;
    }

    public static DrinkRecord create(DrinkType drinkType, DrinkUnit drinkUnit, float quantity) {
        return new DrinkRecord(drinkType, drinkUnit, quantity, null);
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }
}

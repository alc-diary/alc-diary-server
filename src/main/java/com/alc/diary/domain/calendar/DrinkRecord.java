package com.alc.diary.domain.calendar;

import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;
import com.alc.diary.domain.calendar.error.DrinkRecordError;
import com.alc.diary.domain.calendar.vo.DrinkRecordUpdateVo;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.alc.diary.domain.calendar.enums.DrinkType.BEER;
import static com.alc.diary.domain.calendar.enums.DrinkType.WINE;
import static com.alc.diary.domain.user.enums.AlcoholType.SOJU;

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
    @Column(name = "drink_id")
    private Long drinkId;

    @Audited
    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private DrinkUnitType unit;

    @Audited
    @Column(name = "drink_unit_id")
    private Long drinkUnitId;

    @Audited
    @Column(name = "quantity", nullable = false)
    private float quantity;

    @Audited
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private DrinkRecord(DrinkType drinkType, DrinkUnitType drinkUnitType, Long drinkId, Long drinkUnitId, float quantity, LocalDateTime deletedAt) {
        if (drinkType == null) {
            throw new DomainException(DrinkRecordError.NULL_DRINK_TYPE);
        }
        if (drinkUnitType == null) {
            throw new DomainException(DrinkRecordError.NULL_DRINK_UNIT);
        }
        if (!drinkType.isUnitAllowed(drinkUnitType)) {
            throw new DomainException(DrinkRecordError.INVALID_DRINK_UNIT);
        }
        if (quantity == 0) {
            throw new DomainException(DrinkRecordError.ZERO_QUANTITY);
        }
        this.type = drinkType;
        this.unit = drinkUnitType;
        this.drinkId = drinkId;
        this.drinkUnitId = drinkUnitId;
        this.quantity = quantity;
        this.deletedAt = deletedAt;
    }

    public static DrinkRecord create(DrinkType drinkType, DrinkUnitType drinkUnitType, float quantity) {
        Long drinkId;

        switch (drinkType) {
            case BEER:
                drinkId = 1L;
                break;
            case SOJU:
                drinkId = 2L;
                break;
            case WINE:
                drinkId = 3L;
                break;
            case MAKGEOLLI:
                drinkId = 4L;
                break;
            default:
                drinkId= 1L;
        }
        return new DrinkRecord(drinkType, drinkUnitType, drinkId, 1L, quantity, null);
    }

    public static DrinkRecord create(long drinkId, long drinkUnitId, float quantity) {
        return new DrinkRecord(BEER, DrinkUnitType.BOTTLE, drinkId, drinkUnitId, quantity, null);
    }

    public static DrinkRecord create(DrinkType drinkType, DrinkUnitType drinkUnitType, Long drinkId, Long drinkUnitId, float quantity) { // FIXME
        return new DrinkRecord(drinkType, drinkUnitType, drinkId, drinkUnitId, quantity, null);
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }

    public int getTotalPrice() {
        return Math.round(type.getPrice() * quantity);
    }

    public int getTotalCalories() {
        return Math.round(type.getCalories() * quantity);
    }

    public void updateRecord(DrinkRecordUpdateVo updateVo) {
        if (updateVo.drinkType() == null) {
            throw new DomainException(DrinkRecordError.NULL_DRINK_TYPE);
        }
        if (updateVo.drinkUnit() == null) {
            throw new DomainException(DrinkRecordError.NULL_DRINK_UNIT);
        }
        if (!updateVo.drinkType().isUnitAllowed(updateVo.drinkUnit())) {
            throw new DomainException(DrinkRecordError.INVALID_DRINK_UNIT);
        }
        if (quantity == 0) {
            throw new DomainException(DrinkRecordError.ZERO_QUANTITY);
        }
        type = updateVo.drinkType();
        unit = updateVo.drinkUnit();
        quantity = updateVo.quantity();
    }

    public void updateDrinkId(long drinkId) {
        this.drinkId = drinkId;
    }

    public void updateDrinkUnitId(long drinkUnitId) {
        this.drinkUnitId = drinkUnitId;
    }

    public void updateQuantity(float quantity) {
        if (quantity == 0) {
            throw new DomainException(DrinkRecordError.ZERO_QUANTITY);
        }
        this.quantity = quantity;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}

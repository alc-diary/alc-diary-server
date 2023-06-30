package com.alc.diary.domain.calendar;

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

    @Column(name = "drink_unit_info_id", nullable = false, updatable = false)
    private long drinkUnitInfoId;

    @Column(name = "price", nullable = false, updatable = false)
    private int price;

    @Column(name = "calories", nullable = false, updatable = false)
    private int calories;

    @Column(name = "quantity", nullable = false, updatable = false)
    private float quantity;

    public UserCalendarDrink(
            long drinkUnitInfoId,
            int price,
            int calories,
            float quantity
    ) {
        this.drinkUnitInfoId = drinkUnitInfoId;
        this.price = price;
        this.calories = calories;
        this.quantity = quantity;
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }

    public int totalPrice() {
        return Math.round(price * quantity);
    }

    public int totalCalories() {
        return Math.round(calories * quantity);
    }
}

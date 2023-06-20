package com.alc.diary.domain.drink;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.usercalendar.UserCalendar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@ToString(exclude = "userCalendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendar_drinks")
@Entity
public class UserCalendarDrink implements Comparable<UserCalendarDrink> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_calendar_id", foreignKey = @ForeignKey(name = "fk_user_calendar_drinks_user_calendars"))
    private UserCalendar userCalendar;

    @Enumerated(EnumType.STRING)
    @Column(name = "drink", length = 30, nullable = false)
    private Drink drink;

    @Column(name = "quantity", nullable = false)
    private float quantity;

    public UserCalendarDrink(Drink drink, float quantity) {
        if (drink == null) {
            throw new DomainException(UserCalendarDrinkError.DRINK_NULL);
        }
        if (quantity <= 0.0f) {
            throw new DomainException(UserCalendarDrinkError.INVALID_QUANTITY);
        }

        this.drink = drink;
        this.quantity = quantity;
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }

    @Override
    public int compareTo(@NotNull UserCalendarDrink other) {
        return Float.compare(quantity, other.quantity);
    }
}

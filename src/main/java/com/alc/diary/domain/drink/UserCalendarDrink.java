package com.alc.diary.domain.drink;

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
public class UserCalendarDrink implements Comparable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_calendar_id")
    private UserCalendar userCalendar;

    @Enumerated(EnumType.STRING)
    @Column(name = "drink", length = 30, nullable = false)
    private Drink drink;

    @Column(name = "quantity", nullable = false)
    private float quantity;

    public UserCalendarDrink(Drink drink, float quantity) {
        if (drink == null) {
            throw new RuntimeException();
        }
        if (quantity <= 0.0f) {
            throw new RuntimeException();
        }

        this.drink = drink;
        this.quantity = quantity;
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        UserCalendarDrink other = (UserCalendarDrink) o;
        if (quantity > other.quantity) {
            return 1;
        }
        if (quantity < other.quantity) {
            return -1;
        }
        return 0;
    }
}

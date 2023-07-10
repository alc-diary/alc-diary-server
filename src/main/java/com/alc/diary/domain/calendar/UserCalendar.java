package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendars")
@Entity
public class UserCalendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Audited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Audited
    @Column(name = "content", length = 1000)
    private String content;

    @Audited
    @Column(name = "drink_condition", length = 20)
    private String drinkCondition;

    @OneToMany(mappedBy = "userCalendar", cascade = CascadeType.PERSIST)
    private List<DrinkRecord> drinkRecords = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private UserCalendar(long userId, String content, String drinkCondition, LocalDateTime deletedAt) {
        if (StringUtils.length(content) > 1000) {
            throw new DomainException(UserCalendarError.CONTENT_LENGTH_EXCEEDED);
        }
        this.userId = userId;
        this.content = content;
        this.drinkCondition = drinkCondition;
        this.deletedAt = deletedAt;
    }

    public static UserCalendar create(long userId, String content, String condition) {
        return new UserCalendar(userId, content, condition, null);
    }

    public static UserCalendar createTaggedUserCalendar(long userId) {
        return new UserCalendar(userId, null, null, null);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void addDrinkRecords(Iterable<DrinkRecord> drinkRecords) {
        for (DrinkRecord drinkRecord : drinkRecords) {
            addDrinkRecord(drinkRecord);
        }
    }

    public void addDrinkRecord(DrinkRecord drinkRecord) {
        drinkRecords.add(drinkRecord);
        drinkRecord.setUserCalendar(this);
    }

    public boolean isOwner(long userId) {
        return this.userId == userId;
    }

    public void updateContent(long userId, String newContent) {
        if (this.userId != userId) {
            throw new DomainException(UserCalendarError.NO_PERMISSION);
        }
        if (StringUtils.length(newContent) > 1000) {
            throw new DomainException(UserCalendarError.CONTENT_LENGTH_EXCEEDED);
        }
        content = newContent;
    }

    public void updateCondition(long userId, String newCondition) {
        if (this.userId != userId) {
            throw new DomainException(UserCalendarError.NO_PERMISSION);
        }
        content = newCondition;
    }

    public int totalPrice() {
        return drinkRecords.stream()
                .mapToInt(DrinkRecord::getTotalPrice)
                .sum();
    }

    public int totalCalories() {
        return drinkRecords.stream()
                .mapToInt(DrinkRecord::getTotalCalories)
                .sum();
    }
}

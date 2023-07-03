package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calendar.error.UserCalendarError;
import com.alc.diary.domain.calendar.error.UserCalendarImageError;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@ToString(exclude = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_calendars",
        indexes = {
                @Index(name = "idx_user_calendars_user_id", columnList = "user_id"),
                @Index(name = "idx_user_calendars_calendar_id", columnList = "calendar_id")
        }
)
@Entity
public class UserCalendar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Audited
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Audited
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id", foreignKey = @ForeignKey(name = "fk_user_calendars_calendars"))
    private Calendar calendar;

    @Audited
    @Column(name = "content", length = 1000)
    private String content;

    @Audited
    @Column(name = "`condition`", length = 20)
    private String condition;

    @OneToMany(mappedBy = "userCalendar", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserCalendarDrink> drinks = new ArrayList<>();

    @OneToMany(mappedBy = "userCalendar", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserCalendarImage> images = new ArrayList<>();

    @Audited
    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Audited
    @Column(name = "total_calories", nullable = false)
    private int totalCalories;

    @Audited
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    private UserCalendar(
            long userId,
            String content,
            String condition,
            int totalPrice,
            int totalCalories,
            boolean isDeleted
    ) {
        if (StringUtils.length(content) > 1000) {

        }
        this.userId = userId;
        this.content = content;
        this.condition = condition;
        this.totalPrice = totalPrice;
        this.totalCalories = totalCalories;
        this.isDeleted = isDeleted;
    }

    public static UserCalendar create(
            long userId,
            String content,
            String condition
    ) {
        return new UserCalendar(userId, content, condition, 0, 0, false);
    }

    public static UserCalendar createForTaggedUser(long userId) {
        return new UserCalendar(userId, null, null, 0, 0, false);
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void addImages(Iterable<UserCalendarImage> images) {
        for (UserCalendarImage image : images) {
            addImage(image);
        }
    }

    public void addImage(UserCalendarImage userCalendarImage) {
        if (images.size() >= 5) {
            throw new DomainException(UserCalendarImageError.IMAGE_LIMIT_EXCEEDED);
        }
        this.images.add(userCalendarImage);
        userCalendarImage.setUserCalendar(this);
    }

    public void addDrinks(Iterable<UserCalendarDrink> drinks) {
        for (UserCalendarDrink drink : drinks) {
            addDrink(drink);
        }
    }

    public void addDrink(UserCalendarDrink userCalendarDrink) {
        this.drinks.add(userCalendarDrink);
        userCalendarDrink.setUserCalendar(this);

        totalPrice += userCalendarDrink.totalPrice();
        totalCalories += userCalendarDrink.totalCalories();
    }

    public void delete(long userId) {
        if (!isOwner(userId)) {
            throw new DomainException(UserCalendarError.NO_PERMISSION);
        }
        this.isDeleted = true;
    }

    public boolean isOwner(long userId) {
        return this.userId == userId;
    }

    public List<Long> getAllDrinkUnitInfoIds() {
        return drinks.stream()
                .map(UserCalendarDrink::getDrinkUnitInfoId)
                .toList();
    }

    public float getTotalQuantity() {
        return (float) drinks.stream()
                .mapToDouble(UserCalendarDrink::getQuantity)
                .sum();
    }

    public Optional<UserCalendarDrink> getMostConsumedDrink() {
        return drinks.stream()
                .max(Comparator.comparing(UserCalendarDrink::getQuantity));
    }

    public void updateContent(long userId, String newContent) {
        if (!isOwner(userId)) {
            throw new DomainException();
        }
        content = newContent;
    }

    public void updateCondition(long userId, String newCondition) {
        if (!isOwner(userId)) {
            throw new DomainException();
        }
        condition = newCondition;
    }

    public void deleteDrinksByIds(List<Long> userCalendarDrinkIds) {
        drinks.removeAll(drinks.stream()
                .filter(userCalendarDrink -> userCalendarDrinkIds.contains(userCalendarDrink.getId()))
                .toList()
        );
    }
}

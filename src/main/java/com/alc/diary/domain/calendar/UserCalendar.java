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
    @Column(name = "`condition`", length = 20)
    private String condition;

    @OneToMany(mappedBy = "userCalendar")
    private List<DrinkRecord> drinkRecords = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private UserCalendar(long userId, String content, String condition, LocalDateTime deletedAt) {
        this.userId = userId;
        this.content = content;
        this.condition = condition;
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
    }
}

package com.alc.diary.domain.calendar;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "calendars")
@Entity
public class Calendar extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "drink_start_time", nullable = false)
    private LocalDateTime drinkStartTime;

    @Column(name = "drink_end_time", nullable = false)
    private LocalDateTime drinkEndTime;

    public Calendar(User owner, String title, LocalDateTime drinkStartTime, LocalDateTime drinkEndTime) {
        if (owner == null) {
            throw new RuntimeException();
        }
        if (title == null) {
            throw new RuntimeException();
        }
        this.owner = owner;
        this.title = title;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
    }
}

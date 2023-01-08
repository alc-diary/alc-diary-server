package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.model.calender.DrinkReportConverter;
import com.example.alcdiary.domain.model.calender.DrinkReportModel;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Time;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "calender")
@DynamicUpdate
@DynamicInsert
public class Calender extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", updatable = false)
    private String userId;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "drinks", nullable = false)
    private String drinks;
    @Column(name = "hang_over")
    private String hangOver;

    @Column(name = "drink_start_time")
    private Time drinkStartTime;

    @Column(name = "drink_end_time")
    private Time drinkEndTime;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "contents")
    private String contents;

    @Column(name = "drink_report")
    @Convert(converter = DrinkReportConverter.class)
    private DrinkReportModel drinkReport;


    @Builder
    public Calender(
            String userId,
            String title,
            String drinks,
            String hangOver,
            Time drinkStartTime,
            Time drinkEndTime,
            String imageUrl,
            String contents
    ) {
        this.userId = userId;
        this.title = title;
        this.drinks = drinks;
        this.hangOver = hangOver;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
        this.imageUrl = imageUrl;
        this.contents = contents;
    }

    public void update(
            String title,
            String drinks,
            String hangOver,
            Time drinkStartTime,
            Time drinkEndTime,
            String imageUrl,
            String contents) {
        this.title = title;
        this.drinks = drinks;
        this.hangOver = hangOver;
        this.drinkStartTime = drinkStartTime;
        this.drinkEndTime = drinkEndTime;
        this.imageUrl = imageUrl;
        this.contents = contents;

    }
}

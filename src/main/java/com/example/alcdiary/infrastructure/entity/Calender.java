package com.example.alcdiary.infrastructure.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Time;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "calender")
public class Calender extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 20, nullable = false)
    private String title;

    @Column(name = "friends")
    private String friends;
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

    @Column(name = "contents", nullable = false)
    private String contents;


    @Builder
    public Calender(
            String title,
            String contents,
            String drinks,
            String hangOver
    ) {
        this.title = title;
        this.contents = contents;
        this.drinks = drinks;
        this.hangOver = hangOver;
    }
}

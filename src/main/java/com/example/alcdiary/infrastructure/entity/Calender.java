package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.enums.DrinkType;
import lombok.*;

import javax.persistence.*;

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

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "drink_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;

    @Column(name = "hang_over")
    private String hangOver;

    @Builder
    public Calender(
            String title,
            String contents,
            DrinkType drinkType,
            String hangOver
    ) {
        this.title = title;
        this.contents = contents;
        this.drinkType = drinkType;
        this.hangOver = hangOver;
    }
}

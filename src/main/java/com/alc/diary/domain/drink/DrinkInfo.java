package com.alc.diary.domain.drink;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "drink_infos")
@Entity
public class DrinkInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drink_id")
    private Drink drink;

    @Column(name = "drink_unit_id", nullable = false)
    private long drinkUnitId;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "calories", nullable = false)
    private int calories;
}

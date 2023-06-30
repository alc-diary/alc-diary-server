package com.alc.diary.domain.drink;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "drinks",
        indexes = {@Index(name = "idx_drinks_drink_category_id", columnList = "drink_category_id")}
)
@Entity
public class Drink extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drink_category_id", nullable = false)
    private long categoryId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @OneToMany(mappedBy = "drink", cascade = CascadeType.PERSIST)
    private List<DrinkUnitInfo> drinkUnitInfos = new ArrayList<>();
}

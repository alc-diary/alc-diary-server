package com.alc.diary.domain.categoryunit;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkunit.DrinkUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "category_units")
public class CategoryUnit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private DrinkCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private DrinkUnit unit;

    private CategoryUnit(Long id, DrinkCategory category, DrinkUnit unit) {
        this.id = id;
        this.category = category;
        this.unit = unit;
    }

    public static CategoryUnit create(DrinkCategory category, DrinkUnit unit) {
        return new CategoryUnit(null, category, unit);
    }
}

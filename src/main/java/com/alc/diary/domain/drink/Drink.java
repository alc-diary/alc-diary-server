package com.alc.diary.domain.drink;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.drink.enums.DrinkType;
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

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private DrinkType type;

    // @OneToMany(mappedBy = "drink", cascade = CascadeType.PERSIST)
    // private List<DrinkUnitInfo> drinkUnitInfos = new ArrayList<>();

    private Drink(Long id, long categoryId, String name, Long creatorId, Boolean isPublic, DrinkType type) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.creatorId = creatorId;
        this.isPublic = isPublic;
        this.type = type;
    }

    public static Drink createBasicDrink(long categoryId, String name) {
        return new Drink(null, categoryId, name, null, true, DrinkType.BASIC);
    }

    public static Drink createCustomDrink(long categoryId, String name, long creatorId) {
        return new Drink(null, categoryId, name, creatorId, false, DrinkType.CUSTOM);
    }

    // public void addDrinkUnitInfos(List<DrinkUnitInfo> drinkUnitInfos) {
    //     for (DrinkUnitInfo drinkUnitInfo : drinkUnitInfos) {
    //         addDrinkUnitInfo(drinkUnitInfo);
    //     }
    // }
    //
    // public void addDrinkUnitInfo(DrinkUnitInfo drinkUnitInfo) {
    //     drinkUnitInfos.add(drinkUnitInfo);
    //     drinkUnitInfo.setDrink(this);
    // }
}

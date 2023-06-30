package com.alc.diary.domain.drink;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "drink_unit_infos",
        indexes = {
                @Index(name = "idx_drink_unit_infos_drink_id", columnList = "drink_id"),
                @Index(name = "idx_drink_unit_infos_drink_unit_id", columnList = "drink_unit_id")
        }
)
@Entity
public class DrinkUnitInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drink_id", foreignKey = @ForeignKey(name = "fk_drink_unit_infos_drinks"))
    private Drink drink;

    @Column(name = "drink_unit_id", nullable = false)
    private long drinkUnitId;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "calories", nullable = false)
    private int calories;

    public DrinkUnitInfo(
            Long drinkUnitId,
            Integer price,
            Integer calories
    ) {
        this.drinkUnitId = drinkUnitId;
        this.price = price;
        this.calories = calories;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }
}

package com.alc.diary.domain.drinkcategory;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.categoryunit.CategoryUnit;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "drink_categories",
        uniqueConstraints = {@UniqueConstraint(name = "uni_drink_categories_name", columnNames = {"name"})}
)
@Entity
public class DrinkCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "default_drink_brand_id")
    private long defaultDrinkBrandId;

    @OneToMany(mappedBy = "category")
    private List<CategoryUnit> categoryUnits = new ArrayList<>();

    private DrinkCategory(Long id, String name, String imageUrl) {
        if (StringUtils.isBlank(name)) {
            throw new DomainException(DrinkCategoryError.INVALID_INPUT);
        }
        if (StringUtils.length(name) > 30) {
            throw new DomainException(DrinkCategoryError.NAME_LENGTH_EXCEED, "Name: " + name);
        }
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static DrinkCategory create(String name, String imageUrl) { // Default로 지정할 Drink를 생성해서 추가해줘야 함.
        return new DrinkCategory(null, name, imageUrl);
    }

    public void setDefaultDrinkBrandId(long defaultDrinkBrandId) {
        this.defaultDrinkBrandId = defaultDrinkBrandId;
    }
}

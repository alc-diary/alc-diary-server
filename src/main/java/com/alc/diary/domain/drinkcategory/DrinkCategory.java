package com.alc.diary.domain.drinkcategory;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "drink_categories")
@Entity
public class DrinkCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    public DrinkCategory(String name) {
        if (StringUtils.isBlank(name)) {
            throw new DomainException(DrinkCategoryError.INVALID_INPUT);
        }
        if (StringUtils.length(name) > 30) {
            throw new DomainException(DrinkCategoryError.NAME_LENGTH_EXCEED, "Name: " + name);
        }
    }
}

package com.alc.diary.domain.drinkunit;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "drink_units",
        uniqueConstraints = {@UniqueConstraint(name = "uni_drink_units_name", columnNames = {"name"})}
)
@Entity
public class DrinkUnit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    public DrinkUnit(String name) {
        if (StringUtils.isBlank(name)) {
            throw new DomainException(DrinkUnitError.INVALID_INPUT);
        }
        if (StringUtils.length(name) > 30) {
            throw new DomainException(DrinkUnitError.NAME_LENGTH_EXCEED, "name: " + name);
        }
        this.name = name;
    }
}

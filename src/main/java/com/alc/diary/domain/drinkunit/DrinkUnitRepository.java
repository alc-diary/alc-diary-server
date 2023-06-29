package com.alc.diary.domain.drinkunit;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface DrinkUnitRepository extends Repository<DrinkUnit, Long> {

    DrinkUnit save(DrinkUnit drinkUnit);

    List<DrinkUnit> findAll();

    void deleteById(long id);
}

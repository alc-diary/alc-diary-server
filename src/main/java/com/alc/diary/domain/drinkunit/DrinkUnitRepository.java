package com.alc.diary.domain.drinkunit;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DrinkUnitRepository extends Repository<DrinkUnit, Long> {

    DrinkUnit save(DrinkUnit drinkUnit);

    Optional<DrinkUnit> findByName(String name);

    List<DrinkUnit> findAll();

    void deleteById(long id);
}

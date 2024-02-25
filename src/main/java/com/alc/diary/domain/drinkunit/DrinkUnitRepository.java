package com.alc.diary.domain.drinkunit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface DrinkUnitRepository extends Repository<DrinkUnit, Long> {

    DrinkUnit save(DrinkUnit drinkUnit);

    Optional<DrinkUnit> findById(long drinkUnit);

    List<DrinkUnit> findByIdIn(Collection<Long> drinkUnitIds);

    Optional<DrinkUnit> findByName(String name);

    List<DrinkUnit> findAll();

    Page<DrinkUnit> findAll(Pageable pageable);

    void deleteById(long id);
}

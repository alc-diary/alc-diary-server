package com.alc.diary.domain.drinkcategory;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DrinkCategoryRepository extends Repository<DrinkCategory, Long> {

    DrinkCategory save(DrinkCategory drinkCategory);

    List<DrinkCategory> findByIdIn(Iterable<Long> drinkCategoryIds);

    Optional<DrinkCategory> findByName(String name);

    List<DrinkCategory> findAll();

    void deleteById(long id);
}

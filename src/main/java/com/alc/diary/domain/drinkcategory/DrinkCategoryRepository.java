package com.alc.diary.domain.drinkcategory;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface DrinkCategoryRepository extends Repository<DrinkCategory, Long> {

    DrinkCategory save(DrinkCategory drinkCategory);

    List<DrinkCategory> findAll();

    void deletedById(long id);
}

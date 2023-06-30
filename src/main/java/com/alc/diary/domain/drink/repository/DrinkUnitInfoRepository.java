package com.alc.diary.domain.drink.repository;

import com.alc.diary.domain.drink.DrinkUnitInfo;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface DrinkUnitInfoRepository extends Repository<DrinkUnitInfo, Long> {

    DrinkUnitInfo save(DrinkUnitInfo drinkUnitInfo);

    Optional<DrinkUnitInfo> findById(long id);

    List<DrinkUnitInfo> findByIdIn(List<Long> ids);
}

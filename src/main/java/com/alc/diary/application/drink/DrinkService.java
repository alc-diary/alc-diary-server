package com.alc.diary.application.drink;

import com.alc.diary.application.drink.dto.request.AddDrinkUnitInfoRequest;
import com.alc.diary.application.drink.dto.request.CreateDrinkRequest;
import com.alc.diary.application.drink.dto.response.GetAllDrinksResponse;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.DrinkUnitInfo;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.drink.repository.DrinkUnitInfoRepository;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import com.alc.diary.domain.drinkunit.DrinkUnit;
import com.alc.diary.domain.drinkunit.DrinkUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DrinkService {

    private final DrinkRepository drinkRepository;
    private final DrinkUnitInfoRepository drinkUnitInfoRepository;
    private final DrinkCategoryRepository drinkCategoryRepository;
    private final DrinkUnitRepository drinkUnitRepository;

    @Transactional
    public long createDrink(CreateDrinkRequest request) {
        Drink drinkToSave = new Drink(request.drinkCategoryId(), request.drinkName());
        List<DrinkUnitInfo> drinkUnitInfosToSave = request.drinkUnits().stream()
                .map(drinkUnitDto -> new DrinkUnitInfo(
                        drinkUnitDto.drinkUnitId(),
                        drinkUnitDto.price(),
                        drinkUnitDto.calories()
                ))
                .toList();
        drinkToSave.addDrinkUnitInfos(drinkUnitInfosToSave);

        Drink drink = drinkRepository.save(drinkToSave);

        return drink.getId();
    }

    @Transactional
    public long addDrinkUnitInfo(long drinkId, AddDrinkUnitInfoRequest request) {
        Drink drink = drinkRepository.findById(drinkId).orElseThrow();
        DrinkUnitInfo drinkUnitInfoToSave = new DrinkUnitInfo(request.drinkUnitId(), request.price(), request.calories());
        drink.addDrinkUnitInfo(drinkUnitInfoToSave);

        drinkUnitInfoRepository.save(drinkUnitInfoToSave);

        return drinkUnitInfoToSave.getId();
    }

    public List<GetAllDrinksResponse> getAllDrinksResponses() {
        Map<Long, List<Drink>> drinksByCategoryId = drinkRepository.findAll().stream()
                .collect(Collectors.groupingBy(Drink::getCategoryId));
        Map<Long, DrinkUnit> drinkUnitById = drinkUnitRepository.findAll().stream()
                .collect(Collectors.toMap(DrinkUnit::getId, Function.identity()));

        List<DrinkCategory> drinkCategories = drinkCategoryRepository.findByIdIn(drinksByCategoryId.keySet());
        return GetAllDrinksResponse.of(drinkCategories, drinksByCategoryId, drinkUnitById);
    }
}

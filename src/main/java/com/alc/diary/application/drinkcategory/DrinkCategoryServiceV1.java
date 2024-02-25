package com.alc.diary.application.drinkcategory;

import com.alc.diary.application.drink.DrinkDto;
import com.alc.diary.application.drinkunit.DrinkUnitDto;
import com.alc.diary.domain.categoryunit.CategoryUnit;
import com.alc.diary.domain.categoryunit.CategoryUnitRepository;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.drinkcategory.DrinkCategoryError;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DrinkCategoryServiceV1 {

    private final DrinkCategoryRepository drinkCategoryRepository;

    private final DrinkRepository drinkRepository;
    private final CategoryUnitRepository categoryUnitRepository;

    /**
     * 모든 음료 카테고리 조회
     *
     * @return
     */
    public List<DrinkCategoryDto> getAllDrinkCategories() {
        return drinkCategoryRepository.findAll().stream()
                .map(DrinkCategoryDto::fromDomainModel)
                .toList();
    }

    public DrinkCategoryDto getDrinkCategoryById(long drinkCategoryId) {
        return drinkCategoryRepository.findById(drinkCategoryId)
                .map(DrinkCategoryDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(DrinkCategoryError.NOT_FOUND));
    }

    public List<DrinkCategoryDto> getDrinkCategoriesByIds(List<Long> drinkCategoryIds) {
        return drinkCategoryRepository.findByIdIn(drinkCategoryIds).stream()
                .map(DrinkCategoryDto::fromDomainModel)
                .toList();
    }

    public List<DrinkDto> getDrinksByCategoryId(long userId, long categoryId) {
        return drinkRepository.findCreatedOrPublicDrinksByCategoryId(userId, categoryId).stream()
                .map(DrinkDto::fromDomainModel)
                .toList();
    }

    public List<DrinkUnitDto> getDrinkUnitsByCategoryId(long categoryId) {
        return categoryUnitRepository.findByCategoryId(categoryId).stream()
                .map(CategoryUnit::getUnit)
                .map(DrinkUnitDto::fromDomainModel)
                .toList();
    }
}

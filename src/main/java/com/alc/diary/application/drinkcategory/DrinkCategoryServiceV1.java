package com.alc.diary.application.drinkcategory;

import com.alc.diary.application.drink.DrinkDto;
import com.alc.diary.application.drinkcategory.dto.request.CreateDrinkCategoryRequest;
import com.alc.diary.application.drinkcategory.dto.response.GetAllDrinkCategoriesResponse;
import com.alc.diary.application.drinkunit.DrinkUnitDto;
import com.alc.diary.domain.categoryunit.CategoryUnit;
import com.alc.diary.domain.categoryunit.CategoryUnitRepository;
import com.alc.diary.domain.drink.Drink;
import com.alc.diary.domain.drink.repository.DrinkRepository;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkcategory.DrinkCategoryError;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<GetAllDrinkCategoriesResponse> getAllDrinkCategories() {
        return drinkCategoryRepository.findAll().stream()
                .map(GetAllDrinkCategoriesResponse::from)
                .toList();
    }

    public List<DrinkDto> getDrinksByCategoryId(long userId, long categoryId) {
        return drinkRepository.findCreatedOrPublicDrinksByCategoryId(userId, categoryId).stream()
                .map(DrinkDto::from)
                .toList();
    }

    public List<DrinkUnitDto> getDrinkUnitsByCategoryId(long categoryId) {
        return categoryUnitRepository.findByCategoryId(categoryId).stream()
                .map(CategoryUnit::getUnit)
                .map(DrinkUnitDto::fromDomainModel)
                .toList();
    }
}

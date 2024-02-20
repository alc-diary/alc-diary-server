package com.alc.diary.application.admin.drinkcatgory;

import com.alc.diary.application.admin.drinkcatgory.request.AdminCreateDrinkCategoryRequest;
import com.alc.diary.application.admin.drinkunit.DrinkUnitDto;
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
public class AdminDrinkCategoryServiceV1 {

    private final DrinkCategoryRepository drinkCategoryRepository;

    private final CategoryUnitRepository categoryUnitRepository;
    private final DrinkRepository drinkRepository;

    @Transactional
    public long createDrinkCategory(AdminCreateDrinkCategoryRequest request) {
        if (drinkCategoryRepository.findByName(request.name()).isPresent()) {
            throw new DomainException(DrinkCategoryError.DUPLICATE_NAME);
        }
        DrinkCategory drinkCategoryToSave = DrinkCategory.create(request.name(), request.imageUrl());
        DrinkCategory drinkCategory = drinkCategoryRepository.save(drinkCategoryToSave);

        Drink drinkToSave = Drink.createBasicDrink(drinkCategory.getId(), "기본");
        Drink drink = drinkRepository.save(drinkToSave);

        drinkCategory.setDefaultDrinkBrandId(drink.getId());
        return drinkCategory.getId();
    }

    /**
     * 모든 음료 카테고리 조회
     *
     * @return
     */
    public Page<DrinkCategoryDto> getAllDrinkCategories(Pageable pageable) {
        return drinkCategoryRepository.findAll(pageable)
                .map(DrinkCategoryDto::from);
    }

    public DrinkCategoryDto getDrinkCategoryById(long drinkCategoryId) {
        return drinkCategoryRepository.findById(drinkCategoryId)
                .map(DrinkCategoryDto::from)
                .orElseThrow(() -> new DomainException(DrinkCategoryError.NOT_FOUND));
    }

    public List<DrinkUnitDto> getAvailableDrinkUnitsByCategoryId(long categoryId) {
        List<CategoryUnit> categoryUnits = categoryUnitRepository.findByCategoryId(categoryId);

        return categoryUnits.stream()
                .map(categoryUnit -> DrinkUnitDto.from(categoryUnit.getUnit()))
                .toList();
    }
}

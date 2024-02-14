package com.alc.diary.application.admin.drinkunit;

import com.alc.diary.application.admin.drinkunit.request.AdminCreateDrinkUnitRequestV1;
import com.alc.diary.domain.categoryunit.CategoryUnit;
import com.alc.diary.domain.categoryunit.CategoryUnitRepository;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkcategory.DrinkCategoryError;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import com.alc.diary.domain.drinkunit.DrinkUnit;
import com.alc.diary.domain.drinkunit.DrinkUnitRepository;
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
public class AdminDrinkUnitServiceV1 {

    private final DrinkUnitRepository drinkUnitRepository;

    private final DrinkCategoryRepository drinkCategoryRepository;
    private final CategoryUnitRepository categoryUnitRepository;

    @Transactional
    public DrinkUnitDto createDrinkUnit(AdminCreateDrinkUnitRequestV1 request) {
        DrinkUnit drinkUnitToSave = new DrinkUnit(request.name());
        DrinkUnit drinkUnit = drinkUnitRepository.save(drinkUnitToSave);
        return DrinkUnitDto.from(drinkUnit);
    }

    public Page<DrinkUnitDto> getAllDrinkUnits(Pageable pageable) {
        return drinkUnitRepository.findAll(pageable)
                .map(DrinkUnitDto::from);
    }

    public List<DrinkUnitDto> getAvailableDrinkUnitsByCategoryId(long categoryId) {
        List<CategoryUnit> categoryUnits = categoryUnitRepository.findByCategoryId(categoryId);

        return categoryUnits.stream()
                .map(categoryUnit -> DrinkUnitDto.from(categoryUnit.getUnit()))
                .toList();
    }
}

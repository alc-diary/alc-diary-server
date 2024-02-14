package com.alc.diary.application.admin.drinkcatgory;

import com.alc.diary.application.admin.drinkcatgory.request.AdminCreateDrinkCategoryRequest;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkcategory.DrinkCategoryError;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminDrinkCategoryServiceV1 {

    private final DrinkCategoryRepository drinkCategoryRepository;

    @Transactional
    public long createDrinkCategory(AdminCreateDrinkCategoryRequest request) {
        if (drinkCategoryRepository.findByName(request.name()).isPresent()) {
            throw new DomainException(DrinkCategoryError.DUPLICATE_NAME);
        }
        DrinkCategory drinkCategoryToSave = new DrinkCategory(request.name());
        DrinkCategory drinkCategory = drinkCategoryRepository.save(drinkCategoryToSave);
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
}

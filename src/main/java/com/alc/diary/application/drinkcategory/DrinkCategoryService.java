package com.alc.diary.application.drinkcategory;

import com.alc.diary.application.drinkcategory.dto.request.CreateDrinkCategoryRequest;
import com.alc.diary.application.drinkcategory.dto.response.GetAllDrinkCategoriesResponse;
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
public class DrinkCategoryService {

    private final DrinkCategoryRepository drinkCategoryRepository;

    /**
     * 음료 카테고리 생성
     *
     * @param request
     * @return
     */
    @Transactional
    public long createDrinkCategory(CreateDrinkCategoryRequest request) {
        if (drinkCategoryRepository.findByName(request.drinkCategoryName()).isPresent()) {
            throw new DomainException(DrinkCategoryError.DUPLICATE_NAME);
        }
        DrinkCategory drinkCategoryToSave = DrinkCategory.create(request.drinkCategoryName());
        DrinkCategory drinkCategory = drinkCategoryRepository.save(drinkCategoryToSave);
        return drinkCategory.getId();
    }

    /**
     * 모든 음료 카테고리 조회
     *
     * @return
     */
    public Page<GetAllDrinkCategoriesResponse> getAllDrinkCategories(Pageable pageable) {
        return drinkCategoryRepository.findAll(pageable)
                .map(GetAllDrinkCategoriesResponse::from);
    }

    /**
     * 음료 카테고리 삭제
     *
     * @param drinkCategoryId
     */
    @Transactional
    public void deleteDrinkCategory(long drinkCategoryId) {
        drinkCategoryRepository.deleteById(drinkCategoryId);
    }
}

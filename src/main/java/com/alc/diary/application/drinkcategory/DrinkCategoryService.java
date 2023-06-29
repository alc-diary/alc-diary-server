package com.alc.diary.application.drinkcategory;

import com.alc.diary.application.drinkcategory.dto.request.CreateDrinkCategoryRequest;
import com.alc.diary.application.drinkcategory.dto.response.GetAllDrinkCategoriesResponse;
import com.alc.diary.domain.drinkcategory.DrinkCategory;
import com.alc.diary.domain.drinkcategory.DrinkCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        DrinkCategory drinkCategoryToSave = new DrinkCategory(request.drinkCategoryName());
        DrinkCategory drinkCategory = drinkCategoryRepository.save(drinkCategoryToSave);
        return drinkCategory.getId();
    }

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

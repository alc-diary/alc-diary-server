package com.alc.diary.presentation.api;

import com.alc.diary.application.drink.DrinkDto;
import com.alc.diary.application.drinkcategory.DrinkCategoryServiceV1;
import com.alc.diary.application.drinkcategory.dto.request.CreateDrinkCategoryRequest;
import com.alc.diary.application.drinkcategory.dto.response.GetAllDrinkCategoriesResponse;
import com.alc.diary.application.drinkunit.DrinkUnitDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drink-categories")
@RestController
public class DrinkCategoryApiControllerV1 {

    private final DrinkCategoryServiceV1 drinkCategoryServiceV1;

    /**
     * 음료 카테고리를 가져온다.
     *
     * @return 음료 카테고리 목록
     */
    @GetMapping
    private ApiResponse<List<GetAllDrinkCategoriesResponse>> getAllDrinkCategories() {
        return ApiResponse.getSuccess(drinkCategoryServiceV1.getAllDrinkCategories());
    }

    @GetMapping("/{categoryId}/drinks")
    public ApiResponse<List<DrinkDto>> getDrinksByCategoryId(
            @ApiIgnore @RequestAttribute long userId, @PathVariable long categoryId) {
        return ApiResponse.getSuccess(drinkCategoryServiceV1.getDrinksByCategoryId(userId, categoryId));
    }

    @GetMapping("/{categoryId}/units")
    public ApiResponse<List<DrinkUnitDto>> getDrinkUnitsByCategoryId(
            @PathVariable long categoryId
    ) {
        return ApiResponse.getSuccess(drinkCategoryServiceV1.getDrinkUnitsByCategoryId(categoryId));
    }
}

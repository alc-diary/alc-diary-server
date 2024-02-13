package com.alc.diary.presentation.api;

import com.alc.diary.application.drinkcategory.DrinkCategoryService;
import com.alc.diary.application.drinkcategory.dto.request.CreateDrinkCategoryRequest;
import com.alc.diary.application.drinkcategory.dto.response.GetAllDrinkCategoriesResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drink-categories")
@RestController
public class DrinkCategoryApiController {

    private final DrinkCategoryService drinkCategoryService;

    /**
     * 음료 카테고리를 생성한다.
     *
     * @param request 음료 카테고리 생성 요청
     * @return 생성된 음료 카테고리 ID
     */
    @PostMapping
    private ApiResponse<Long> createDrinkCategory(
            @RequestBody CreateDrinkCategoryRequest request
    ) {
        return ApiResponse.getCreated(drinkCategoryService.createDrinkCategory(request));
    }

    /**
     * 음료 카테고리를 가져온다.
     *
     * @return 음료 카테고리 목록
     */
    @GetMapping
    private ApiResponse<Page<GetAllDrinkCategoriesResponse>> getAllDrinkCategories(Pageable pageable) {
        return ApiResponse.getSuccess(drinkCategoryService.getAllDrinkCategories(pageable));
    }

    /**
     * 음료 카테고리를 삭제한다.
     *
     * @param drinkCategoryId 음료 카테고리 ID
     * @return 성공
     */
    @DeleteMapping("/{drinkCategoryId}")
    private ApiResponse<Void> deleteDrinkCategory(
            @PathVariable long drinkCategoryId
    ) {
        drinkCategoryService.deleteDrinkCategory(drinkCategoryId);
        return ApiResponse.getSuccess();
    }
}

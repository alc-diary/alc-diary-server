package com.alc.diary.presentation.api;

import com.alc.diary.application.drinkcategory.DrinkCategoryService;
import com.alc.diary.application.drinkcategory.dto.request.CreateDrinkCategoryRequest;
import com.alc.diary.application.drinkcategory.dto.response.GetAllDrinkCategoriesResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drink-categories")
@RestController
public class DrinkCategoryApiController {

    private final DrinkCategoryService drinkCategoryService;

    @PostMapping
    private ApiResponse<Long> createDrinkCategory(
            @RequestBody CreateDrinkCategoryRequest request
    ) {
        return ApiResponse.getCreated(drinkCategoryService.createDrinkCategory(request));
    }

    @GetMapping
    private ApiResponse<List<GetAllDrinkCategoriesResponse>> getAllDrinkCategories() {
        return ApiResponse.getSuccess(drinkCategoryService.getAllDrinkCategories());
    }

    @DeleteMapping("/{drinkCategoryId}")
    private ApiResponse<Void> deleteDrinkCategory(
            @PathVariable long drinkCategoryId
    ) {
        drinkCategoryService.deleteDrinkCategory(drinkCategoryId);
        return ApiResponse.getSuccess();
    }
}

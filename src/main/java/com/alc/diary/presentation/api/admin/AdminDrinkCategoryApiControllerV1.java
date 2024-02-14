package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.drinkcatgory.AdminDrinkCategoryServiceV1;
import com.alc.diary.application.admin.drinkcatgory.request.AdminCreateDrinkCategoryRequest;
import com.alc.diary.application.admin.drinkcatgory.DrinkCategoryDto;
import com.alc.diary.application.admin.drinkunit.DrinkUnitDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/drink-categories")
@RestController
public class AdminDrinkCategoryApiControllerV1 {

    private final AdminDrinkCategoryServiceV1 adminDrinkCategoryServiceV1;

    /**
     * 음료 카테고리를 생성한다.
     *
     * @param request 음료 카테고리 생성 요청
     * @return 생성된 음료 카테고리 ID
     */
    @PostMapping
    public ApiResponse<Long> createDrinkCategory(
            @RequestBody AdminCreateDrinkCategoryRequest request
    ) {
        return ApiResponse.getCreated(adminDrinkCategoryServiceV1.createDrinkCategory(request));
    }

    /**
     * 음료 카테고리를 가져온다.
     *
     * @return 음료 카테고리 목록
     */
    @GetMapping
    public ApiResponse<Page<DrinkCategoryDto>> getAllDrinkCategories(Pageable pageable) {
        return ApiResponse.getSuccess(adminDrinkCategoryServiceV1.getAllDrinkCategories(pageable));
    }

    @GetMapping("/{categoryId}/available-units")
    public ApiResponse<List<DrinkUnitDto>> getAvailableDrinkUnitsByCategoryId(@PathVariable long categoryId) {
        return ApiResponse.getSuccess(adminDrinkCategoryServiceV1.getAvailableDrinkUnitsByCategoryId(categoryId));
    }
}

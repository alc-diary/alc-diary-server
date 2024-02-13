package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.drinkcatgory.AdminDrinkCategoryServiceV1;
import com.alc.diary.application.admin.drinkcatgory.request.AdminCreateDrinkCategoryRequest;
import com.alc.diary.application.admin.drinkcatgory.response.AdminGetAllDrinkCategoriesResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/v1/admin/drink-categories")
@RestController
public class AdminDrinkCategoryApiControllerV1 {

    private AdminDrinkCategoryServiceV1 adminDrinkCategoryServiceV1;

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
    public ApiResponse<Page<AdminGetAllDrinkCategoriesResponse>> getAllDrinkCategories(Pageable pageable) {
        return ApiResponse.getSuccess(adminDrinkCategoryServiceV1.getAllDrinkCategories(pageable));
    }
}

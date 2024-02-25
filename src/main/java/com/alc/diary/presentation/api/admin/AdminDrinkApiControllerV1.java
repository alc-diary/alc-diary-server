package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.drink.AdminDrinkServiceV1;
import com.alc.diary.application.admin.drink.AdminDrinkDto;
import com.alc.diary.application.admin.drink.request.AdminCreateDrinkRequest;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/drinks")
@RestController
public class AdminDrinkApiControllerV1 {

    private final AdminDrinkServiceV1 adminDrinkServiceV1;

    @PostMapping
    public ApiResponse<AdminDrinkDto> createDrink(@RequestBody AdminCreateDrinkRequest request) {
        return ApiResponse.getCreated(adminDrinkServiceV1.createDrink(request));
    }

    @GetMapping
    public ApiResponse<Page<AdminDrinkDto>> getAllDrinks(Pageable pageable) {
        return ApiResponse.getSuccess(adminDrinkServiceV1.getAllDrinks(pageable));
    }
}

package com.alc.diary.presentation.api.admin.drinkunit;

import com.alc.diary.application.admin.drinkunit.AdminDrinkUnitServiceV1;
import com.alc.diary.application.admin.drinkunit.DrinkUnitDto;
import com.alc.diary.application.admin.drinkunit.request.AdminCreateDrinkUnitRequestV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/drink-units")
@RestController
public class AdminDrinkUnitApiControllerV1 {

    private final AdminDrinkUnitServiceV1 adminDrinkUnitServiceV1;

    @PostMapping
    public ApiResponse<DrinkUnitDto> createDrinkUnit(@RequestBody AdminCreateDrinkUnitRequestV1 request) {
        return ApiResponse.getCreated(adminDrinkUnitServiceV1.createDrinkUnit(request));
    }

    @GetMapping
    public ApiResponse<Page<DrinkUnitDto>> getAllDrinkUnits(Pageable pageable) {
        return ApiResponse.getSuccess(adminDrinkUnitServiceV1.getAllDrinkUnits(pageable));
    }
}

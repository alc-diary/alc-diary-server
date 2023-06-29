package com.alc.diary.presentation.api;

import com.alc.diary.application.drinkunit.DrinkUnitService;
import com.alc.diary.application.drinkunit.dto.request.CreateDrinkUnitRequest;
import com.alc.diary.application.drinkunit.dto.response.GetAllDrinkUnitsResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drink-units")
@RestController
public class DrinkUnitApiController {

    private final DrinkUnitService drinkUnitService;

    @PostMapping
    public ApiResponse<Long> createDrinkUnit(
            @RequestBody CreateDrinkUnitRequest request
    ) {
        return ApiResponse.getCreated(drinkUnitService.createDrinkUnit(request));
    }

    @GetMapping
    public ApiResponse<List<GetAllDrinkUnitsResponse>> getAllDrinkUnits() {
        return ApiResponse.getSuccess(drinkUnitService.getAllDrinkUnits());
    }

    @DeleteMapping("/{drinkUnitId}")
    public ApiResponse<Void> deleteDrinkUnit(
            @PathVariable long drinkUnitId
    ) {
        drinkUnitService.deleteDrinkUnit(drinkUnitId);
        return ApiResponse.getSuccess();
    }
}

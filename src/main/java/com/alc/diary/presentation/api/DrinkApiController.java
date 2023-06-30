package com.alc.diary.presentation.api;

import com.alc.diary.application.drink.DrinkService;
import com.alc.diary.application.drink.dto.request.AddDrinkUnitInfoRequest;
import com.alc.diary.application.drink.dto.request.CreateDrinkRequest;
import com.alc.diary.application.drink.dto.response.GetAllDrinksResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/drinks")
@RestController
public class DrinkApiController {

    private final DrinkService drinkService;

    @PostMapping
    public ApiResponse<Long> createDrink(
            @Validated @RequestBody CreateDrinkRequest request
    ) {
        return ApiResponse.getCreated(drinkService.createDrink(request));
    }

    @GetMapping
    public ApiResponse<List<GetAllDrinksResponse>> getAllDrinks() {
        return ApiResponse.getSuccess(drinkService.getAllDrinksResponses());
    }

    @PostMapping("/{drinkId}/drink-unit-info")
    public ApiResponse<Long> addDrinkUnitInfo(
            @PathVariable long drinkId,
            @RequestBody AddDrinkUnitInfoRequest request
    ) {
        return ApiResponse.getCreated(drinkService.addDrinkUnitInfo(drinkId, request));
    }
}

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

    /**
     * 음료를 생성한다.
     *
     * @param request 음료 생성 요청
     * @return 생성된 음료 ID
     */
    @PostMapping
    public ApiResponse<Long> createDrink(
            @Validated @RequestBody CreateDrinkRequest request
    ) {
        return ApiResponse.getCreated(drinkService.createDrink(request));
    }

    /**
     * 음료를 가져온다.
     *
     * @return 음료 목록
     */
    @GetMapping
    public ApiResponse<List<GetAllDrinksResponse>> getAllDrinks() {
        return ApiResponse.getSuccess(drinkService.getAllDrinksResponses());
    }

    /**
     * 음료 단위 정보를 추가한다.
     *
     * @param drinkId 음료 ID
     * @param request 음료 단위 정보 추가 요청
     * @return 생성된 음료 단위 정보 ID
     */
    @PostMapping("/{drinkId}/drink-unit-info")
    public ApiResponse<Long> addDrinkUnitInfo(
            @PathVariable long drinkId,
            @RequestBody AddDrinkUnitInfoRequest request
    ) {
        return ApiResponse.getCreated(drinkService.addDrinkUnitInfo(drinkId, request));
    }
}

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

    /**
     * 음료 단위를 생성한다.
     *
     * @param request 음료 단위 생성 요청
     * @return 생성된 음료 단위 ID
     */
    @PostMapping
    public ApiResponse<Long> createDrinkUnit(
            @RequestBody CreateDrinkUnitRequest request
    ) {
        return ApiResponse.getCreated(drinkUnitService.createDrinkUnit(request));
    }

    /**
     * 음료 단위를 가져온다.
     *
     * @return 음료 단위 목록
     */
    @GetMapping
    public ApiResponse<List<GetAllDrinkUnitsResponse>> getAllDrinkUnits() {
        return ApiResponse.getSuccess(drinkUnitService.getAllDrinkUnits());
    }

    /**
     * 음료 단위를 삭제한다.
     *
     * @param drinkUnitId 음료 단위 ID
     * @return 성공
     */
    @DeleteMapping("/{drinkUnitId}")
    public ApiResponse<Void> deleteDrinkUnit(
            @PathVariable long drinkUnitId
    ) {
        drinkUnitService.deleteDrinkUnit(drinkUnitId);
        return ApiResponse.getSuccess();
    }
}

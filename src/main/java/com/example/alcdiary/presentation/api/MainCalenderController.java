package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.presentation.dto.request.CreateCalenderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calender/main")
public class MainCalenderController {
    private final CalenderUseCase calenderUseCase;

    /*
     *  TODO: 캘린더 멘트 조회
     *  오늘 기준으로 (day) 값 조회, 목표 일동안 얼마나 마셨는지 계산 필요(user)
     */
//    @GetMapping(value = "")


    /**
     * 캘린더 간편저장(메인)
     */
    @PostMapping(value = "")
    public void createSimple(
            @RequestBody CreateCalenderRequest request,
            @RequestAttribute UserIdModel userIdModel
    ) {
        calenderUseCase.createSimple(
                CreateCalenderCommand.builder()
                        .userId(userIdModel.parse())
                        .drinks(request.getDrinks())
                        .drinkStartTime(request.getDrinkStartTime())
                        .build()
        );
    }
}

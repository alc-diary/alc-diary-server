package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.domain.model.user.UserIdModel;
import com.example.alcdiary.presentation.dto.request.CreateCalenderRequest;
import com.example.alcdiary.presentation.dto.request.UpdateCalenderRequest;
import com.example.alcdiary.presentation.dto.response.FindCalenderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calender")
public class CalenderController {
    private final CalenderUseCase calenderUseCase;

    /**
     * 캘린더 상세 조회
     */
    @GetMapping(value = "/{calenderId}")
    public FindCalenderResponse find(
            @RequestAttribute UserIdModel userIdModel,
            @PathVariable(name = "calenderId") Long calenderId
    ) {
        return calenderUseCase.find(userIdModel.parse(), calenderId).toResponse();
    }

    /**
     * 캘린더 월별 조회 / 일별 조회
     */
    @GetMapping(value = "/search")
    public void search(
            @RequestParam Integer month,
            @RequestParam Integer day
    ) {

    }

    /***
     * 캘린더 저장
     */
    @PostMapping(value = "")
    public void create(
            @RequestBody CreateCalenderRequest request,
            @RequestAttribute UserIdModel userIdModel) {
        calenderUseCase.create(
                CreateCalenderCommand.builder()
                        .userId(userIdModel.parse())
                        .title(request.getTitle())
                        .friends(request.getFriends())
                        .drinks(request.getDrinks())
                        .hangOver(request.getHangOver())
                        .drinkStartTime(request.getDrinkStartTime())
                        .drinkEndTime(request.getDrinkEndTime())
                        .imageUrl(request.getImageUrl())
                        .contents(request.getContents())
                        .build()
        );
    }

    /**
     * 캘린더 내용 수정
     */
    @PatchMapping(value = "/{calenderId}")
    public void update(@PathVariable Long calenderId,
                       @RequestAttribute UserIdModel userIdModel,
                       @RequestBody UpdateCalenderRequest request) {
        calenderUseCase.update(
                UpdateCalenderCommand.builder()
                        .userId(userIdModel.parse())
                        .title(request.getTitle())
                        .friends(request.getFriends())
                        .drinks(request.getDrinks())
                        .hangOver(request.getHangOver())
                        .drinkStartTime(request.getDrinkStartTime())
                        .drinkEndTime(request.getDrinkEndTime())
                        .imageUrl(request.getImageUrl())
                        .contents(request.getContents())
                        .build(), calenderId
        );
    }

    /**
     * 캘린더 삭제, 권한
     */
    @DeleteMapping(value = "/{calenderId}")
    public void delete(
            @PathVariable Long calenderId,
            @RequestAttribute UserIdModel userIdModel) {
        calenderUseCase.delete(calenderId, userIdModel.parse());
    }
}

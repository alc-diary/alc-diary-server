package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.CalenderUseCase;
import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.presentation.dto.request.CreateCalenderRequest;
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
    @GetMapping(value = "/{userId}/{calenderId}")
    public FindCalenderResponse find(
            @PathVariable String userId,
            @PathVariable(name = "calenderId") Long calenderId
    ) {
        return calenderUseCase.find(userId,calenderId).toResponse();
    }

    /**
     *  캘린더 월별 조회 / 일별 조회
     */
    @GetMapping(value = "")
    public void  search() {

    }

    @PostMapping(value = "/{userId}")
    public void create(
            @RequestBody CreateCalenderRequest request,
            @PathVariable String userId) {
        calenderUseCase.create(
                CreateCalenderCommand.builder()
                        .userId(userId)
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
     *  캘린더 내용 수정
     */
    @PutMapping(value = "")
    public void update() {

    }

    /**
     *  캘린더 삭제, 권한
     */
    @DeleteMapping(value = "/{calenderId}")
    public void delete(
            @PathVariable String calenderId,
            @RequestParam UserIdModel userIdModel) {

    }
}

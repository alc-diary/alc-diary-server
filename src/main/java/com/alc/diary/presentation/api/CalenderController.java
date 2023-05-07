package com.alc.diary.presentation.api;

import com.alc.diary.application.calender.CalenderService;
import com.alc.diary.application.calender.dto.request.SaveCalenderRequest;
import com.alc.diary.application.calender.dto.request.UpdateCalenderRequest;
import com.alc.diary.application.calender.dto.response.FindCalenderDetailResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/calender")
public class CalenderController {

    private final CalenderService calenderService;

    @GetMapping(value = "/{calenderId}")
    public ApiResponse<FindCalenderDetailResponse> find(@PathVariable Long calenderId) {
        return ApiResponse.getSuccess(calenderService.find(calenderId));
    }

    @GetMapping(value = "search")
    public void search(
            @RequestParam(value = "month") Integer month,
            @RequestParam(value = "day", required = false) Integer day,
            @RequestAttribute Long userId
    ) {
        // month, day
    }

    @PostMapping(value = "")
    public void save(@RequestBody @Validated SaveCalenderRequest request,
                     @RequestAttribute Long userId) {
        calenderService.save(request, userId);
    }

    @PutMapping(value = "/{calenderId}")
    public void update(@PathVariable Long calenderId,
                       @RequestAttribute Long userId,
                       @RequestBody UpdateCalenderRequest request) {
        calenderService.update(calenderId, userId, request);
    }

    @DeleteMapping(value = "/{calenderId}")
    public void delete(@PathVariable Long calenderId,
                       @RequestAttribute Long userId) {
        calenderService.delete(calenderId, userId);
    }
}

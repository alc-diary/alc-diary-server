package com.alc.diary.presentation.api;


import com.alc.diary.application.calender.MainCalenderService;
import com.alc.diary.application.calender.dto.request.SaveMainCalenderRequest;
import com.alc.diary.application.calender.dto.response.GetMainResponse;
import com.alc.diary.application.calender.dto.response.MainCalenderResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/calender/main")
public class MainCalenderController {

    private final MainCalenderService mainCalenderService;

    /**
     * 메인 간편저장
     */
    @PostMapping(value = "")
    public ApiResponse<MainCalenderResponse> save(
            @RequestBody SaveMainCalenderRequest request,
            @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(mainCalenderService.saveMain(request, userId));
    }

    /**
     * 메인 목표, 멘트 조회하기
     */
    @GetMapping(value = "")
    public ApiResponse<GetMainResponse> getMain(@RequestAttribute Long userId) {
        return ApiResponse.getSuccess(mainCalenderService.getMain(userId));
    }
}

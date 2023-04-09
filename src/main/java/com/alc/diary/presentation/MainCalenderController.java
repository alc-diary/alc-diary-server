package com.alc.diary.presentation;


import com.alc.diary.application.calender.MainCalenderService;
import com.alc.diary.application.calender.dto.request.SaveMainCalenderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calender/main")
public class MainCalenderController {

    private final MainCalenderService mainCalenderService;

    /**
     * 메인 간편저장
     */
    @PostMapping(value = "")
    public void save(
            @RequestBody SaveMainCalenderRequest request,
            @RequestAttribute Long userId
    ) {
        mainCalenderService.saveMain(request, userId);
    }

    /**
     * 메인 목표, 멘트 조회하기
     */
    @GetMapping(value = "")
    public void getMain(@RequestAttribute Long userId) {
        mainCalenderService.getMain(userId);
    }
}

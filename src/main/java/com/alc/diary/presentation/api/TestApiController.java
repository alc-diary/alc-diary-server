package com.alc.diary.presentation.api;

import com.alc.diary.application.calender.CalenderService;
import com.alc.diary.application.calender.dto.response.FindCalenderDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestApiController {

    private final CalenderService calenderService;

    @GetMapping
    public void test(@RequestParam String user) {
        log.info("user: {}", user);
    }

    @GetMapping("/timezone")
    public LocalDateTime timezone() {
        return LocalDateTime.now();
    }

    @GetMapping("/calender/{calenderId}")
    public FindCalenderDetailResponse getCalender(
            @PathVariable Long calenderId
    ) {
        return calenderService.find(calenderId);
    }
}

package com.alc.diary.presentation.api;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestApiController {

    private final CalenderRepository calenderRepository;

    @GetMapping
    public void test(@RequestParam String user) {
        log.info("user: {}", user);
    }

    @GetMapping("/timezone")
    public LocalDateTime timezone() {
        return LocalDateTime.now();
    }

    @GetMapping("/calender/{calenderId}")
    public Calender getCalender(
            @PathVariable Long calenderId
    ) {
        return calenderRepository.getCalenderById(calenderId).orElse(null);
    }
}

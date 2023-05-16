package com.alc.diary.presentation.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestApiController {

    @GetMapping
    public void test(@RequestParam String user) {
        log.info("user: {}", user);
    }

    @GetMapping("/timezone")
    public LocalDateTime timezone() {
        return LocalDateTime.now();
    }
}

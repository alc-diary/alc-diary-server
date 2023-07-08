package com.alc.diary.presentation.api;

import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestApiController {

    private final UserRepository userRepository;

    @GetMapping
    public void test(@RequestParam String user) {
        log.info("user: {}", user);
    }

    @GetMapping("/timezone")
    public LocalDateTime timezone() {
        return LocalDateTime.now();
    }

    @GetMapping("/local-date-time")
    public void localDateTimeTest() {
        LocalDateTime now = LocalDateTime.now();
    }

    @GetMapping("/deploy")
    public String deploy() {
        return "good";
    }
}

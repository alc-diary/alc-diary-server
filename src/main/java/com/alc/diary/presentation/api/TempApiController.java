package com.alc.diary.presentation.api;

import com.alc.diary.application.temp.TempService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/temp")
@RestController
public class TempApiController {

    private final TempService tempService;

    @GetMapping("/migration")
    public void migration(@RequestParam long id) {
        tempService.migration(id);
    }
}

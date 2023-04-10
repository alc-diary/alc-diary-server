package com.alc.diary.presentation.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calender")
public class CalenderController {
    @PostMapping(value = "/")
    public void save() {

    }

    @PutMapping(value = "/{calenderId}")
    // 권한 체크
    public void update(@PathVariable Long calenderId) {

    }

    @DeleteMapping(value = "/{calenderId}")
    public void delete(@PathVariable Long calenderId) {

    }
}

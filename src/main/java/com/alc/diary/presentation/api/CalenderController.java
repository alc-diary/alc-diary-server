package com.alc.diary.presentation.api;

import com.alc.diary.application.calender.CalenderService;
import com.alc.diary.application.calender.dto.request.SaveCalenderRequest;
import com.alc.diary.application.calender.dto.request.UpdateCalenderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/calender")
public class CalenderController {

    private final CalenderService calenderService;

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

package com.alc.diary.presentation.api.calendar;

import com.alc.diary.application.calendar.CalendarServiceV2;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponseV2;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/v2/calendars")
@RestController
public class CalendarApiControllerV2 {

    private final CalendarServiceV2 calendarServiceV2;

    @PostMapping
    public ApiResponse<CreateCalendarResponseV2> createCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody CreateCalendarRequestV2 request
            ) {
        return ApiResponse.getCreated(calendarServiceV2.createCalendarAndGenerateResponse(userId, request));
    }
}

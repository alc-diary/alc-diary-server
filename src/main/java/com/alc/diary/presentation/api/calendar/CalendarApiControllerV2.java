package com.alc.diary.presentation.api.calendar;

import com.alc.diary.application.calendar.CalendarDto;
import com.alc.diary.application.calendar.CalendarServiceV2;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponseV2;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsResponseV2;
import com.alc.diary.application.drink.DrinkDto;
import com.alc.diary.presentation.dto.ApiResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.YearMonth;
import java.util.List;

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

    @GetMapping("/{calendarId}")
    public ApiResponse<CalendarDto> getCalendarById(@PathVariable long calendarId) {
        return ApiResponse.getSuccess(calendarServiceV2.getCalendarById(calendarId));
    }

    @GetMapping("/monthly")
    public ApiResponse<List<GetMonthlyCalendarsResponseV2>> getMonthlyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM") @RequestParam(name = "yearMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        return ApiResponse.getSuccess(calendarServiceV2.getMonthlyCalendars(userId, yearMonth));
    }
}

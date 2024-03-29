package com.alc.diary.presentation.api.calendar;

import com.alc.diary.application.calendar.CalendarDto;
import com.alc.diary.application.calendar.CalendarServiceV2;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequestV2;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponseV2;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsResponseV2;
import com.alc.diary.presentation.dto.ApiResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
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

    @PatchMapping("/{calendarId}")
    public ApiResponse<Void> updateCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @Validated @RequestBody UpdateCalendarRequestV2 request
    ) {
        calendarServiceV2.updateCalendar(userId, calendarId, request);
        return ApiResponse.getSuccess();
    }

    @GetMapping("/daily")
    public ApiResponse<List<CalendarDto>> getDailyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM-dd") @RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ApiResponse.getSuccess(calendarServiceV2.getDailyCalendars(userId, date));
    }

    @GetMapping("/monthly")
    public ApiResponse<List<GetMonthlyCalendarsResponseV2>> getMonthlyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM") @RequestParam(name = "yearMonth", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        return ApiResponse.getSuccess(calendarServiceV2.getMonthlyCalendars(userId, yearMonth));
    }
}

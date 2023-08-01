package com.alc.diary.presentation.api;

import com.alc.diary.application.calendar.CalendarService;
import com.alc.diary.application.calendar.dto.request.CreateCalendarFromMainRequest;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.request.CreateCommentRequest;
import com.alc.diary.application.calendar.dto.request.UpdateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.*;
import com.alc.diary.presentation.dto.ApiResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/calendars")
@RestController
public class CalendarApiController {

    private final CalendarService calendarService;

    @PostMapping
    public ApiResponse<CreateCalendarResponse> createCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody CreateCalendarRequest request
    ) {
        return ApiResponse.getCreated(calendarService.createCalendarAndGenerateResponse(userId, request));
    }

    @GetMapping("/{calendarId}")
    public ApiResponse<GetCalendarByIdResponse> getCalendarById(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId
    ) {
        return ApiResponse.getSuccess(calendarService.getCalendarById(userId, calendarId));
    }

    @PatchMapping("/{calendarId}/user-calendars/{userCalendarId}")
    public ApiResponse<Void> updateCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @PathVariable long userCalendarId,
            @RequestBody UpdateCalendarRequest request
    ) {
        calendarService.updateCalendar(userId, calendarId, userCalendarId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/{calendarId}")
    public ApiResponse<Void> updateCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @RequestBody UpdateCalendarRequest request
    ) {
        calendarService.updateCalendar(userId, calendarId, request);
        return ApiResponse.getSuccess();
    }

    @DeleteMapping("/{calendarId}/user-calendars/{userCalendarId}")
    public ApiResponse<Void> deleteUserCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @PathVariable long userCalendarId
    ) {
        calendarService.deleteUserCalendar(userId, calendarId, userCalendarId);
        return ApiResponse.getSuccess();
    }

    @GetMapping("/daily")
    public ApiResponse<List<GetDailyCalendarsResponse>> getDailyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM-dd") @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "zoneId", defaultValue = "Asia/Seoul") ZoneId zoneId
    ) {
        return ApiResponse.getSuccess(calendarService.getDailyCalendars(userId, date, zoneId));
    }

    @GetMapping("/monthly")
    public ApiResponse<List<GetMonthlyCalendarsResponse>> getMonthlyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM") @RequestParam(name = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            @RequestParam(name = "zoneId", defaultValue = "Asia/Seoul") ZoneId zoneId
    ) {
        return ApiResponse.getSuccess(calendarService.getMonthlyCalendars(userId, month, zoneId));
    }

    @PostMapping("/main")
    public ApiResponse<Long> createCalendarFromMain(
            @ApiIgnore long userId,
            @Validated @RequestBody CreateCalendarFromMainRequest request
    ) {
        return ApiResponse.getSuccess(calendarService.createCalendarFromMain(userId, request));
    }

    // @PostMapping("/{calendarId}/comments")
    public ApiResponse<Long> createComment(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @RequestBody CreateCommentRequest request
    ) {
        return null;
    }

    // @GetMapping("/{calendarId}/comments")
    public ApiResponse<GetCalendarCommentsByCalendarIdResponse> getCalendarCommentsByCalendarId() {
        return null;
    }
}

package com.alc.diary.presentation.api;

import com.alc.diary.application.calendar.CalendarAppService;
import com.alc.diary.application.calendar.dto.request.FindCalendarAppResponse;
import com.alc.diary.application.calendar.dto.request.SaveCalendarAppRequest;
import com.alc.diary.application.calendar.dto.request.SearchCalendarAppRequest;
import com.alc.diary.application.calendar.dto.response.GetCalendarRequestsAppResponse;
import com.alc.diary.application.calendar.dto.response.GetMonthlyCalendarsAppResponse;
import com.alc.diary.application.calendar.dto.response.SearchCalendarAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/v1/calendar")
@RestController
public class CalendarApiController {

    private final CalendarAppService calendarAppService;

    @PostMapping
    public ApiResponse<?> save(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody SaveCalendarAppRequest request
    ) {
        calendarAppService.save(userId, request);
        return ApiResponse.getCreated();
    }

    @GetMapping("/{calendarId}")
    public ApiResponse<FindCalendarAppResponse> find(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId
    ) {
        return ApiResponse.getSuccess(calendarAppService.find(userId, calendarId));
    }

    @GetMapping("/search")
    public ApiResponse<SearchCalendarAppResponse> search(
            @ApiIgnore @RequestAttribute long userId,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "day", required = false) Integer day
    ) {
        return ApiResponse.getSuccess(
                calendarAppService.search(
                        userId,
                        new SearchCalendarAppRequest(query, year, month, day)
                ));
    }

    @GetMapping("/monthly")
    public ApiResponse<GetMonthlyCalendarsAppResponse> getMonthlyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month
    ) {
        return ApiResponse.getSuccess(
                calendarAppService.getMonthlyCalendars(userId, year, month)
        );
    }

    @GetMapping("/requests")
    public ApiResponse<GetCalendarRequestsAppResponse> getCalendarRequests(
            @ApiIgnore @RequestAttribute long userId
    ) {
        return ApiResponse.getSuccess(
                calendarAppService.getCalendarRequests(userId)
        );
    }

    @PutMapping("/{calendarId}")
    public ApiResponse<?> update(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @RequestBody String request
    ) {
        return null;
    }

    @DeleteMapping("/{calendarId}")
    public ApiResponse<?> delete(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId
    ) {
        return null;
    }
}

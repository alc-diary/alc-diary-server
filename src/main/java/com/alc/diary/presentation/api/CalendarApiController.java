package com.alc.diary.presentation.api;

import com.alc.diary.application.calendar.CalendarAppService;
import com.alc.diary.application.calendar.dto.request.CreateCalendarRequest;
import com.alc.diary.application.calendar.dto.response.CreateCalendarResponse;
import com.alc.diary.application.calendar.dto.response.GetCalendarByIdResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/calendars")
@RestController
public class CalendarApiController {

    private final CalendarAppService calendarAppService;

    @PostMapping
    public ApiResponse<CreateCalendarResponse> createCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody CreateCalendarRequest request
    ) {
        return ApiResponse.getCreated(calendarAppService.createCalendar(userId, request));
    }

    @GetMapping("/{calendarId}")
    public ApiResponse<GetCalendarByIdResponse> getCalendarById(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId
    ) {
        return ApiResponse.getSuccess(calendarAppService.getCalendarByIdResponse(userId, calendarId));
    }

//    @PostMapping
//    public ApiResponse<Void> createCalendar(
//            @ApiIgnore @RequestAttribute long userId,
//            @Validated @RequestBody CreateCalendarAppRequest request
//    ) {
//        calendarAppService.createCalendar(userId, request);
//        return ApiResponse.getCreated();
//    }
//
//    @GetMapping("/{calendarId}")
//    public ApiResponse<CalendarDto> find(
//            @ApiIgnore @RequestAttribute long userId,
//            @PathVariable long calendarId
//    ) {
//        return ApiResponse.getSuccess(calendarAppService.findCalendar(userId, calendarId));
//    }
//
//    @GetMapping("/search")
//    public ApiResponse<List<CalendarDto>> search(
//            @ApiIgnore @RequestAttribute long userId,
//            @RequestParam(value = "query", required = false) String query,
//            @RequestParam(value = "year", required = false) Integer year,
//            @RequestParam(value = "month", required = false) Integer month,
//            @RequestParam(value = "day", required = false) Integer day
//    ) {
//        return ApiResponse.getSuccess(
//                calendarAppService.search(
//                        userId,
//                        new SearchCalendarAppRequest(query, year, month, day)
//                ));
//    }
//
//    @GetMapping("/monthly")
//    public ApiResponse<GetMonthlyCalendarsAppResponse> getMonthlyCalendars(
//            @ApiIgnore @RequestAttribute long userId,
//            @RequestParam(value = "year", required = false) Integer year,
//            @RequestParam(value = "month", required = false) Integer month
//    ) {
//        return ApiResponse.getSuccess(
//                calendarAppService.getMonthlyCalendars(userId, year, month)
//        );
//    }
//
//    @GetMapping("/requests")
//    public ApiResponse<GetCalendarRequestsAppResponse> getCalendarRequests(
//            @ApiIgnore @RequestAttribute long userId
//    ) {
//        return ApiResponse.getSuccess(
//                calendarAppService.getCalendarRequests(userId)
//        );
//    }
//
////    @PutMapping("/{calendarId}")
//    public ApiResponse<?> update(
//            @ApiIgnore @RequestAttribute long userId,
//            @PathVariable long calendarId,
//            @RequestBody String request
//    ) {
//        return null;
//    }
//
////    @DeleteMapping("/{calendarId}")
//    public ApiResponse<?> delete(
//            @ApiIgnore @RequestAttribute long userId,
//            @PathVariable long calendarId
//    ) {
//        return null;
//    }
}

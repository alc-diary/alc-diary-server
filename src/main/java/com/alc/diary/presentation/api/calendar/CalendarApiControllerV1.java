package com.alc.diary.presentation.api.calendar;

import com.alc.diary.application.calendar.CalendarServiceV1;
import com.alc.diary.application.calendar.dto.request.*;
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

@Deprecated(since = "2024-03-01", forRemoval = true)
@RequiredArgsConstructor
@RequestMapping("/v1/calendars")
@RestController
public class CalendarApiControllerV1 {

    private final CalendarServiceV1 calendarServiceV1;

    /**
     * 캘린더를 생성한다.
     *
     * @param userId 사용자 ID
     * @param request 캘린더 생성 요청
     * @return 생성된 캘린더 ID
     */
    @PostMapping
    public ApiResponse<CreateCalendarResponse> createCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody CreateCalendarRequest request
    ) {
        return ApiResponse.getCreated(calendarServiceV1.createCalendarAndGenerateResponse(userId, request));
    }

    /**
     * 캘린더를 가져온다.
     *
     * @param userId 사용자 ID
     * @param calendarId 캘린더 ID
     * @return 캘린더
     */
    @GetMapping("/{calendarId}")
    public ApiResponse<GetCalendarByIdResponse> getCalendarById(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId
    ) {
        return ApiResponse.getSuccess(calendarServiceV1.getCalendarById(userId, calendarId));
    }

    /**
     * 캘린더를 수정한다.
     *
     * @param userId 사용자 ID
     * @param calendarId 캘린더 ID
     * @param userCalendarId 사용자 캘린더 ID
     * @param request 캘린더 수정 요청
     * @return 성공
     */
    @PatchMapping("/{calendarId}/user-calendars/{userCalendarId}")
    public ApiResponse<Void> updateCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @PathVariable long userCalendarId,
            @RequestBody UpdateUserCalendarRequest request
    ) {
        calendarServiceV1.updateUserCalendar(userId, calendarId, userCalendarId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 캘린더를 수정한다.
     *
     * @param userId 사용자 ID
     * @param calendarId 캘린더 ID
     * @param request 캘린더 수정 요청
     * @return 성공
     */
    @PutMapping("/{calendarId}")
    public ApiResponse<Void> updateCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @RequestBody UpdateCalendarRequest request
    ) {
        calendarServiceV1.updateCalendar(userId, calendarId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 사용자 캘린더를 삭제한다.
     *
     * @param userId 사용자 ID
     * @param calendarId 캘린더 ID
     * @param userCalendarId 사용자 캘린더 ID
     * @return 성공
     */
    @DeleteMapping("/{calendarId}/user-calendars/{userCalendarId}")
    public ApiResponse<Void> deleteUserCalendar(
            @ApiIgnore @RequestAttribute long userId,
            @PathVariable long calendarId,
            @PathVariable long userCalendarId
    ) {
        calendarServiceV1.deleteUserCalendar(userId, calendarId, userCalendarId);
        return ApiResponse.getSuccess();
    }

    /**
     * 일일 캘린더를 조회한다.
     *
     * @param userId 사용자 ID
     * @param date 날짜
     * @param zoneId 타임존
     * @return 일일 캘린더
     */
    @GetMapping("/daily")
    public ApiResponse<List<GetDailyCalendarsResponse>> getDailyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM-dd") @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "zoneId", defaultValue = "Asia/Seoul") ZoneId zoneId
    ) {
        return ApiResponse.getSuccess(calendarServiceV1.getDailyCalendars(userId, date, zoneId));
    }

    /**
     * 월간 캘린더를 조회한다.
     *
     * @param userId 사용자 ID
     * @param month 월
     * @param zoneId 타임존
     * @return 월간 캘린더
     */
    @GetMapping("/monthly")
    public ApiResponse<List<GetMonthlyCalendarsResponse>> getMonthlyCalendars(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM") @RequestParam(name = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            @RequestParam(name = "zoneId", defaultValue = "Asia/Seoul") ZoneId zoneId
    ) {
        return ApiResponse.getSuccess(calendarServiceV1.getMonthlyCalendars(userId, month, zoneId));
    }

    /**
     * 메인에서 캘린더를 생성한다.
     *
     * @param userId 사용자 ID
     * @param request 메인에서 캘린더 생성 요청
     * @return 생성된 캘린더 ID
     */
    @PostMapping("/main")
    public ApiResponse<Long> createCalendarFromMain(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody CreateCalendarFromMainRequest request
    ) {
        return ApiResponse.getSuccess(calendarServiceV1.createCalendarFromMain(userId, request));
    }

    /**
     * 메인 캘린더에 필요한 데이터를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 메인 캘린더에 필요한 데이터
     */
    @GetMapping("/main")
    public ApiResponse<GetMainResponse> getMain(
            @ApiIgnore @RequestAttribute long userId
    ) {
        return ApiResponse.getSuccess(calendarServiceV1.getMain(userId));
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

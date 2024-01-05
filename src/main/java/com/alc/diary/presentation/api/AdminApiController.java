package com.alc.diary.presentation.api;

import com.alc.diary.application.admin.AdminService;
import com.alc.diary.application.admin.response.CalendarDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@CrossOrigin(originPatterns = "**")
@RequestMapping("/v1/admin")
@RestController
public class AdminApiController {

    private final AdminService adminService;

    @GetMapping("/calendars")
    public ApiResponse<Page<CalendarDto>> getAllCalendars(Pageable pageable, HttpServletResponse response) {
        Page<CalendarDto> allCalendars = adminService.getAllCalendars(pageable);
        response.addHeader("X-Total-Count", String.valueOf(allCalendars.getTotalElements()));
        return ApiResponse.getSuccess(allCalendars);
    }

    @GetMapping("/calendars/{calendarId}")
    public ApiResponse<CalendarDto> getCalendar(@PathVariable Long calendarId) {
        return ApiResponse.getSuccess(adminService.getCalendar(calendarId));
    }
}

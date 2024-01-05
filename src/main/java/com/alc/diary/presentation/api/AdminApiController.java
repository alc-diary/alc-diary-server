package com.alc.diary.presentation.api;

import com.alc.diary.application.admin.AdminService;
import com.alc.diary.application.admin.response.CalendarDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin")
@RestController
public class AdminApiController {

    private final AdminService adminService;

    @GetMapping("/calendar")
    public ApiResponse<Page<CalendarDto>> getAllCalendars(Pageable pageable) {
        return ApiResponse.getSuccess(adminService.getAllCalendars(pageable));
    }

    @GetMapping("/calendar/{calendarId}")
    public ApiResponse<CalendarDto> getCalendar(@PathVariable Long calendarId) {
        return ApiResponse.getSuccess(adminService.getCalendar(calendarId));
    }
}

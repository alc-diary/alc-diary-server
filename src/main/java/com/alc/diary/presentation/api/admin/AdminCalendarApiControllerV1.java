package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.calendar.AdminCalendarService;
import com.alc.diary.application.admin.calendar.response.AdminCalendarDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/calendars")
@RestController
public class AdminCalendarApiControllerV1 {

    private final AdminCalendarService adminCalendarService;

    @GetMapping
    public ApiResponse<Page<AdminCalendarDto>> getAllCalendars(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.getSuccess(adminCalendarService.getAllCalendars(pageable));
    }

    @GetMapping("/{calendarId}")
    public ApiResponse<AdminCalendarDto> getCalendarById(@PathVariable long calendarId) {
        return ApiResponse.getSuccess(adminCalendarService.getCalendarById(calendarId));
    }
}

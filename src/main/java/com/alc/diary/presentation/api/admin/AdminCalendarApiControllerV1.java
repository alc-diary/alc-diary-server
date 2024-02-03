package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.AdminCalendarService;
import com.alc.diary.application.admin.response.CalendarDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin/api/v1/calendars")
@RestController
public class AdminCalendarApiControllerV1 {

    private final AdminCalendarService adminCalendarService;

    @CrossOrigin(origins = "*")
    @GetMapping
    public ApiResponse<Page<CalendarDto>> getAllCalendars(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResponse.getSuccess(adminCalendarService.getAllCalendars(pageable));
    }
}

package com.alc.diary.presentation.api;

import com.alc.diary.application.report.ReportAppService;
import com.alc.diary.application.report.dto.response.GetMonthlyReportAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/reports")
public class ReportApiController {

    private final ReportAppService reportAppService;

    @GetMapping
    public ApiResponse<GetMonthlyReportAppResponse> getMonthlyReport(
            @RequestAttribute Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        if (month == null) {
            month = LocalDate.now().getMonthValue();
        }
        return ApiResponse.getSuccess(reportAppService.getMonthlyReport(userId, year, month));
    }
}

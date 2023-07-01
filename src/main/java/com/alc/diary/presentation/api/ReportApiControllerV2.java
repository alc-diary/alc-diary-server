package com.alc.diary.presentation.api;

import com.alc.diary.application.report.ReportAppService;
import com.alc.diary.application.report.ReportService;
import com.alc.diary.application.report.dto.response.GetMonthlyReportResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.YearMonth;
import java.time.ZoneId;

@RequiredArgsConstructor
@RequestMapping("/v1/reports")
@RestController
public class ReportApiControllerV2 {

    public final ReportService reportService;

    @GetMapping("/monthly")
    public ApiResponse<GetMonthlyReportResponse> getMonthlyReport(
            @ApiIgnore @RequestAttribute long userId,
            @ApiParam(value = "yyyy-MM") @RequestParam(name = "month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        return ApiResponse.getSuccess(reportService.getMonthlyReport(userId, month, zoneId));
    }
}

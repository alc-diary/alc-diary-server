package com.alc.diary.presentation.api;

import com.alc.diary.application.report.ReportAppService;
import com.alc.diary.application.report.dto.response.GetMonthlyReportAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/report")
public class ReportApiController {

    private final ReportAppService reportAppService;

    /**
     * 월간 리포트를 가져온다.
     *
     * @param userId 사용자 ID
     * @param year 년
     * @param month 월
     * @return 월간 리포트
     */
    @GetMapping
    public ApiResponse<GetMonthlyReportAppResponse> getMonthlyReport(
            @ApiIgnore @RequestAttribute Long userId,
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

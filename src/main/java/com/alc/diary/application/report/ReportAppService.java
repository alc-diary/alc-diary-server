package com.alc.diary.application.report;

import com.alc.diary.application.report.dto.response.BeverageSummaryDto;
import com.alc.diary.application.report.dto.response.DrinkingDaySummaryDto;
import com.alc.diary.application.report.dto.response.GetMonthlyReportAppResponse;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.Report;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportAppService {

    private final CalenderRepository calenderRepository;

    public GetMonthlyReportAppResponse getMonthlyReport(Long userId, int year, int month) {
        LocalDateTime currentMonthStart = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime currentMonthEnd = currentMonthStart.plusMonths(1L);
        List<Calender> currentMonthCalenders =
                calenderRepository.findByUser_IdAndDrinkStartDateTimeGreaterThanEqualAndDrinkStartDateTimeLessThan(
                        userId,
                        currentMonthStart,
                        currentMonthEnd
                );
        Report currentMonthReport = new Report(currentMonthCalenders);

        LocalDateTime lastMonthStart = currentMonthStart.minusMonths(1L);
        LocalDateTime lastMonthEnd = currentMonthStart;
        List<Calender> lastMonthCalenders = calenderRepository.findByUser_IdAndDrinkStartDateTimeGreaterThanEqualAndDrinkStartDateTimeLessThan(
                userId,
                lastMonthStart,
                lastMonthEnd
        );
        Report lastMonthReport = new Report(lastMonthCalenders);

        return new GetMonthlyReportAppResponse(
                currentMonthReport.totalDrinkQuantity(),
                currentMonthReport.totalDrinkQuantity() - lastMonthReport.totalDrinkQuantity(),
                currentMonthReport.totalDrinkingDays(),
                currentMonthReport.totalDrinkingDays() - lastMonthReport.totalDrinkingDays(),
                new BeverageSummaryDto(currentMonthReport.mostConsumedBeverageSummary()),
                new DrinkingDaySummaryDto(currentMonthReport.mostFrequentDrinkingDaySummary())
        );
    }
}

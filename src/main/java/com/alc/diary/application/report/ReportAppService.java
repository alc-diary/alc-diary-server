package com.alc.diary.application.report;

import com.alc.diary.application.report.dto.response.DrinkSummaryDto;
import com.alc.diary.application.report.dto.response.DrinkingDaySummaryDto;
import com.alc.diary.application.report.dto.response.GetMonthlyReportAppResponse;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.Report;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportAppService {

    private final CalenderRepository calenderRepository;

    @Cacheable(value = "monthlyReport", key = "#userId + '_' + #year + '-' + #month", cacheManager = "cacheManager")
    public GetMonthlyReportAppResponse getMonthlyReport(Long userId, int year, int month) {
        LocalDateTime currentMonthStart = LocalDateTime.of(year, month, 1, 0, 0);

        Report currentMonthReport = getCurrentMonthReport(userId, currentMonthStart);
        Report lastMonthReport = getLastMonthReport(userId, currentMonthStart);

        return new GetMonthlyReportAppResponse(
                currentMonthReport.totalDrinkQuantity(),
                currentMonthReport.totalDrinkQuantity() - lastMonthReport.totalDrinkQuantity(),
                currentMonthReport.totalDrinkingDays(),
                currentMonthReport.totalDrinkingDays() - lastMonthReport.totalDrinkingDays(),
                currentMonthReport.totalSpendMoney(),
                currentMonthReport.totalCalories(),
                0,
                new DrinkSummaryDto(currentMonthReport.mostConsumedDrinkSummary()),
                new DrinkingDaySummaryDto(currentMonthReport.mostFrequentDrinkingDaySummary())
        );
    }

    private Report getCurrentMonthReport(Long userId, LocalDateTime currentMonthStart) {
        LocalDateTime currentMonthEnd = currentMonthStart.plusMonths(1L);
        List<Calender> currentMonthCalenders = getMonthlyCalenders(userId, currentMonthStart, currentMonthEnd);
        return new Report(currentMonthCalenders);
    }

    private Report getLastMonthReport(Long userId, LocalDateTime currentMonthStart) {
        LocalDateTime lastMonthStart = currentMonthStart.minusMonths(1L);
        LocalDateTime lastMonthEnd = currentMonthStart;
        List<Calender> lastMonthCalenders = getMonthlyCalenders(userId, lastMonthStart, lastMonthEnd);
        return new Report(lastMonthCalenders);
    }

    private List<Calender> getMonthlyCalenders(Long userId, LocalDateTime currentMonthStart, LocalDateTime currentMonthEnd) {
        return calenderRepository.findByUser_IdAndDrinkStartDateTimeGreaterThanEqualAndDrinkStartDateTimeLessThan(
                userId,
                currentMonthStart,
                currentMonthEnd
        );
    }
}

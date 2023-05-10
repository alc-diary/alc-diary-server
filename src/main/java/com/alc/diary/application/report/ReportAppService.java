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
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime startPlusOneMonth = start.plusMonths(1L);
        LocalDateTime end = LocalDateTime.of(startPlusOneMonth.getYear(), startPlusOneMonth.getMonth(), 1, 0, 0);

        List<Calender> findCalenders =
                calenderRepository.findByUser_IdAndDrinkStartDateTimeGreaterThanEqualAndDrinkStartDateTimeLessThan(
                        userId,
                        start,
                        end
                );
        Report report = new Report(findCalenders);

        return new GetMonthlyReportAppResponse(
                report.totalDrinkQuantity(),
                report.totalDrinkingDays(),
                new BeverageSummaryDto(report.mostConsumedBeverageSummary()),
                new DrinkingDaySummaryDto(report.mostFrequentDrinkingDaySummary())
        );
    }
}

package com.alc.diary.application.report;

import com.alc.diary.application.report.dto.response.GetMonthlyReportAppResponse;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.Report;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportAppService {

    private final CalenderRepository calenderRepository;

    public GetMonthlyReportAppResponse getMonthlyReport(Long userId) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime nowPlusOneMonth = now.plusMonths(1L);
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(nowPlusOneMonth.getYear(), nowPlusOneMonth.getMonth(), 1, 0, 0);
        List<Calender> findCalenders = calenderRepository.findByDrinkStartDateTimeGreaterThanEqualAndDrinkStartDateTimeLessThan(start, end);
        Report report = new Report(findCalenders);
        report.getMostDrunkDayOfWeek();
        return new GetMonthlyReportAppResponse(
                report.getNumberOfDrinks(),
                report.getDaysOfDrinking(),
                report.getMostDrunkAlcoholType(),
                report.getMostDrunkDayOfWeek()
        );
    }
}

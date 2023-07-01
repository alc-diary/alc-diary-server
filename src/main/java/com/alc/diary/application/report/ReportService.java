package com.alc.diary.application.report;

import com.alc.diary.application.report.dto.response.GetMonthlyReportResponse;
import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.report.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportService {

    private final CalendarRepository calendarRepository;

    public GetMonthlyReportResponse getMonthlyReport(long userId, YearMonth month, ZoneId zoneId) {
        ZonedDateTime rangeStart = month.atDay(1).atStartOfDay(zoneId);
        ZonedDateTime rangeEnd = month.plusMonths(1).atDay(1).atStartOfDay(zoneId);
        Calendars calendars =
                Calendars.from(calendarRepository.findCalendarsWithInRangeForSpecificUser(userId, rangeStart, rangeEnd));
        Report report = new Report(calendars);
        return GetMonthlyReportResponse.from(report);
    }
}

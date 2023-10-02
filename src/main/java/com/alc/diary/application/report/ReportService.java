package com.alc.diary.application.report;

import com.alc.diary.application.report.dto.response.GetMonthlyReportResponse;
import com.alc.diary.domain.calendar.Calendars;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.report.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReportService {

    //    private final CalendarLegacyRepository calendarLegacyRepository;
    private final CalendarRepository calendarRepository;

    @Cacheable(value = "monthlyReportLegacy", key = "#userId + '_' + #month.year + '-' + #month.monthValue", cacheManager = "cacheManager")
    public GetMonthlyReportResponse getMonthlyReport(long userId, YearMonth month, ZoneId zoneId) {
        ZonedDateTime rangeStart = month.atDay(1).atStartOfDay(zoneId);
        ZonedDateTime rangeEnd = month.plusMonths(1).atDay(1).atStartOfDay(zoneId);
        Calendars calendars = new Calendars(
                calendarRepository.findUserCalendarsForSpecificUserWithRangeAndUserId(userId, rangeStart, rangeEnd),
                zoneId
        );
        Report report = new Report(calendars);

        ZonedDateTime lastMonthRangeStart = month.minusMonths(1).atDay(1).atStartOfDay(zoneId);
        ZonedDateTime lastMonthRangeEnd = lastMonthRangeStart.plusMonths(1);
        Calendars lastMonthCalendars = new Calendars(
                calendarRepository.findUserCalendarsForSpecificUserWithRangeAndUserId(userId, lastMonthRangeStart, lastMonthRangeEnd),
                zoneId
        );
        Report lastMonthReport = new Report(lastMonthCalendars);

        return GetMonthlyReportResponse.from(report, lastMonthReport);
    }
}

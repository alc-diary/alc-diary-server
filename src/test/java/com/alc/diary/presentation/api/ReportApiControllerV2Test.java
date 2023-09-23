package com.alc.diary.presentation.api;

import com.alc.diary.application.report.ReportService;
import com.alc.diary.application.report.dto.response.GetMonthlyReportResponse;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = ReportApiControllerV2.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class ReportApiControllerV2Test {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @DisplayName("유저 ID와 YearMonth로 월간 레포트를 조회한다.")
    @Test
    void shouldReturnMonthlyReportWhenUserIdAndYearMonthProvided() throws Exception {
        // given
        long userId = 1L;
        YearMonth yearMonth = YearMonth.of(2023, 1);
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        GetMonthlyReportResponse response = new GetMonthlyReportResponse(
                1.0f,
                0.5f,
                10,
                4,
                10000,
                2000,
                50,
                2,
                DrinkType.BEER,
                DayOfWeek.MONDAY,
                "2023-01-03T00:00:00.000000"
        );

        given(reportService.getMonthlyReport(userId, yearMonth, zoneId))
                .willReturn(response);

        // when & then
        mvc.perform(get("/v1/reports/monthly")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("userId", userId)
                        .param("month", yearMonth.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.totalBottlesConsumed").value(1.0))
                .andExpect(jsonPath("$.data.totalBottlesConsumedDiffFromLastMonth").value(0.5))
                .andExpect(jsonPath("$.data.totalDaysDrinking").value(10))
                .andExpect(jsonPath("$.data.totalDaysDrinkingDiffFromLastMonth").value(4))
                .andExpect(jsonPath("$.data.totalSpentOnDrinks").value(10000))
                .andExpect(jsonPath("$.data.totalCaloriesFromDrinks").value(2000))
                .andExpect(jsonPath("$.data.totalRunningTimeToBurnCalories").value(50))
                .andExpect(jsonPath("$.data.riceSoupEquivalent").value(2))
                .andExpect(jsonPath("$.data.mostFrequentlyConsumedDrink").value("BEER"))
                .andExpect(jsonPath("$.data.mostFrequentlyDrinkingDay").value("MONDAY"))
                .andExpect(jsonPath("$.data.lastDrinkingDate").value("2023-01-03T00:00:00.000000"));
        then(reportService).should().getMonthlyReport(userId, yearMonth, zoneId);
    }
}

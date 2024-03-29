package com.alc.diary.domain.report;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.Calenders;
import com.alc.diary.domain.calender.DrinkSummary;
import com.alc.diary.domain.calender.Report;
import com.alc.diary.domain.calender.enums.DrinkType;
import com.alc.diary.domain.calender.model.CalenderImage;
import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.enums.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.alc.diary.domain.calender.enums.DrinkType.SOJU;
import static org.assertj.core.api.Assertions.assertThat;

class ReportTest {

    private com.alc.diary.domain.calender.Report report;

    @BeforeEach
    void setUp() {
        List<DrinkModel> drinkModels = List.of(
                new DrinkModel(DrinkType.BEER, 1.5f),
                new DrinkModel(SOJU, 3.0f)
        );
        Calenders calenders = new Calenders(IntStream.rangeClosed(1, 11)
                .mapToObj(i -> Calender.builder()
                        .id((long) i)
                        .title("test title" + i)
                        .contents("test contents" + i)
                        .drinkStartDateTime(LocalDateTime.of(2023, 5, 7, 0, 0).plusHours(i * 12L))
                        .drinkEndDateTime(LocalDateTime.of(2023, 5, 7, 10, 10).plusHours(i * 12L))
                        .drinkModels(drinkModels)
                        .image(new CalenderImage("test image" + i))
                        .drinkCondition("😀")
                        .user(User.builder(SocialType.KAKAO, "123").build())
                        .build())
                .collect(Collectors.toList()));
        report = new com.alc.diary.domain.calender.Report(calenders);
    }

    @Test
    void getNumberOfDrinks() {
        assertThat(report.totalDrinkQuantity())
                .isEqualTo(49.5f);
    }

    // 수정 예정
    @Test
    void getNumberOfDrinks_empty_list() {
        com.alc.diary.domain.calender.Report testReport = new com.alc.diary.domain.calender.Report(Collections.emptyList());
        assertThat(testReport.totalDrinkQuantity())
                .isEqualTo(0.0f);
    }

    @Test
    void getDaysOfDrinking() {
        assertThat(report.totalDrinkingDays())
                .isEqualTo(6);
    }

    @Test
    void getDaysOfDrinking_empty_list() {
        com.alc.diary.domain.calender.Report testReport = new com.alc.diary.domain.calender.Report(Collections.emptyList());
        assertThat(testReport.totalDrinkingDays())
                .isEqualTo(0);
    }

    @Test
    void 가장_많이_마신_주종() {
        DrinkSummary expect = new DrinkSummary(SOJU, 33.0f);
        assertThat(report.mostConsumedDrinkSummary())
                .isEqualTo(expect);
    }

    @Test
    void 가장_많이_마신_주종_empty_list() {
        com.alc.diary.domain.calender.Report testReport = new Report(Collections.emptyList());
        assertThat(testReport.mostConsumedDrinkSummary())
                .isEqualTo(DrinkSummary.EMPTY);
    }
}

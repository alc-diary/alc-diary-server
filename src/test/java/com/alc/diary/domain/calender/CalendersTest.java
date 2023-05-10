package com.alc.diary.domain.calender;

import com.alc.diary.domain.calender.enums.DrinkType;
import com.alc.diary.domain.calender.model.CalenderImage;
import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.alc.diary.domain.calender.enums.DrinkType.SOJU;
import static org.assertj.core.api.Assertions.assertThat;

class CalendersTest {

    private Calenders calenders;

    @BeforeEach
    void setUp() {
        List<DrinkModel> drinkModels = List.of(
                new DrinkModel(DrinkType.BEER, 1.5f),
                new DrinkModel(SOJU, 3.0f)
        );
        calenders = new Calenders(IntStream.rangeClosed(1, 11)
                                           .mapToObj(i -> Calender.builder()
                                                                  .id((long) i)
                                                                  .title("test title" + i)
                                                                  .contents("test contents" + i)
                                                                  .drinkStartDateTime(LocalDateTime.of(2023, 5, 7, 0, 0).plusHours(i * 12L))
                                                                  .drinkEndDateTime(LocalDateTime.of(2023, 5, 7, 10, 10).plusHours(i * 12L))
                                                                  .drinkModels(drinkModels)
                                                                  .image(new CalenderImage("test image" + i))
                                                                  .drinkCondition("😀")
                                                                  .user(new User())
                                                                  .build())
                                           .collect(Collectors.toList())
        );
    }

    @Test
    void getNumberOfDrinks() {
        assertThat(calenders.calculateTotalDrinkQuantity())
                .isEqualTo(49.5f);
    }

    // 수정 예정
    @Test
    void getNumberOfDrinks_empty_list() {
        Calenders testCalenders = new Calenders(Collections.emptyList());
        assertThat(testCalenders.calculateTotalDrinkQuantity())
                .isEqualTo(0.0f);
    }

    @Test
    void getDaysOfDrinking() {
        assertThat(calenders.calculateTotalDrinkingDays())
                .isEqualTo(6);
    }

    // 수정 예정
    @Test
    void getDaysOfDrinking_empty_list() {
        Calenders testCalenders = new Calenders(Collections.emptyList());
        assertThat(testCalenders.calculateTotalDrinkingDays())
                .isEqualTo(0);
    }

    @Test
    void 가장_많이_마신_주종() {
        assertThat(calenders.calculateMostConsumedBeverageSummary())
                .isEqualTo(SOJU);
    }

    @Test
    void 가장_많이_마신_주종_empty_list() {
        Calenders testCalenders = new Calenders(Collections.emptyList());
        assertThat(testCalenders.calculateMostConsumedBeverageSummary())
                .isNull();
    }
}

package com.alc.diary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeTest {

    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.of(2023, 5, 7, 0, 0);
    }

    @Test
    void format_테스트() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime testLocalDateTime = LocalDateTime.of(2023, 5, 7, 10, 10);

        assertThat(formatter.format(localDateTime))
                .isEqualTo(formatter.format(testLocalDateTime));
    }
}

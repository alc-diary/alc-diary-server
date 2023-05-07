package com.alc.diary.domain.calender.repository;

import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.enums.QueryType;

import java.time.LocalDate;
import java.util.List;

public interface CustomCalenderRepository {
    List<Calender> search(long userId, QueryType queryType, LocalDate date);
}

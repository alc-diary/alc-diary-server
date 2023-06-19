package com.alc.diary.application.calendar.dto.command;

import com.alc.diary.domain.calendar.Calendar;

import java.util.List;

public interface SearchCommand {

    List<Calendar> execute(long userId);
}

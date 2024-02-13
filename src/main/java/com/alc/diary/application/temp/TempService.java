package com.alc.diary.application.temp;

import com.alc.diary.domain.calendar.Calendar;
import com.alc.diary.domain.calendar.DrinkRecord;
import com.alc.diary.domain.calendar.Photo;
import com.alc.diary.domain.calendar.UserCalendar;
import com.alc.diary.domain.calendar.enums.DrinkType;
import com.alc.diary.domain.calendar.enums.DrinkUnitType;
import com.alc.diary.domain.calendar.repository.CalendarRepository;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TempService {

    private final CalendarRepository calendarRepository;
    private final CalenderRepository calenderRepository;

    @Transactional
    public void migration(long id) {
        List<Calender> calenders = calenderRepository.findByIdGreaterThanEqual(id);

        List<Calendar> calendarListToSave = calenders.stream().map(calender -> {
                    Calendar calendar = Calendar.create(
                            calender.getUser().getId(),
                            calender.getTitle(),
                            (float) calender.getDrinkModels().stream()
                                    .mapToDouble(DrinkModel::getQuantity)
                                    .sum(),
                            ZonedDateTime.of(calender.getDrinkStartDateTime(), ZoneId.of("Asia/Seoul")),
                            ZonedDateTime.of(calender.getDrinkEndDateTime(), ZoneId.of("Asia/Seoul")
                            ));

                    UserCalendar userCalendar = UserCalendar.create(calender.getUser().getId(), calender.getContents(), calender.getDrinkCondition());
                    if (calender.getDrinkModels() != null) {
                        List<DrinkRecord> drinkRecords = calender.getDrinkModels().stream()
                                .filter(drinkModel -> drinkModel.getType() != null)
                                .map(drinkModel -> DrinkRecord.create(
                                        DrinkType.valueOf(drinkModel.getType().toString()),
                                        DrinkUnitType.BOTTLE,
                                        drinkModel.getQuantity()
                                ))
                                .toList();
                        userCalendar.addDrinkRecords(drinkRecords);
                    }
                    calendar.addUserCalendar(userCalendar);
                    if (calender.getImage() != null && calender.getImage().getImages() != null) {
                        List<Photo> photos = calender.getImage().getImages().stream()
                                .map(url -> Photo.create(calender.getUser().getId(), url))
                                .toList();
                        calendar.addPhotos(photos);
                    }
                    return calendar;
                })
                .toList();

        calendarRepository.saveAll(calendarListToSave);
    }
}

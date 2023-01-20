package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.application.command.CreateCalenderCommand;
import com.example.alcdiary.application.command.SearchCalenderCommand;
import com.example.alcdiary.application.command.UpdateCalenderCommand;
import com.example.alcdiary.domain.enums.DrinkType;
import com.example.alcdiary.domain.enums.EditRole;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CalenderError;
import com.example.alcdiary.domain.model.calender.CalenderModel;
import com.example.alcdiary.domain.model.calender.DrinksModel;
import com.example.alcdiary.domain.model.calender.SearchCalenderModel;
import com.example.alcdiary.domain.model.calender.UserCalenderModel;
import com.example.alcdiary.domain.service.CalenderService;
import com.example.alcdiary.infrastructure.domain.repository.impl.UserCalenderRepositoryImpl;
import com.example.alcdiary.infrastructure.entity.Calender;
import com.example.alcdiary.infrastructure.entity.UserCalender;
import com.example.alcdiary.infrastructure.jpa.CalenderRepository;
import com.example.alcdiary.infrastructure.jpa.UserCalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CalenderServiceImpl implements CalenderService {
    private final CalenderRepository calenderRepository;
    private final UserCalenderRepositoryImpl userCalenderRepositoryImpl;
    private final UserCalenderRepository userCalenderRepository;
    private static final String DEFAULT_TITLE = "오늘의 음주 기록";

    @Override
    public CalenderModel find(String userId, Long calenderId) {
        try {
            UserCalenderModel result = userCalenderRepositoryImpl.findCalenders(userId, calenderId);
            Calender calender = result.getCalender();
            return CalenderModel.builder()
                    .id(calender.getId())
                    .title(calender.getTitle())
                    .friends(result.getFriends().toArray(new String[0]))
                    .drinkStartTime(calender.getDrinkStartTime())
                    .drinkEndTime(calender.getDrinkEndTime())
                    .createdAt(calender.getCreatedAt())
                    .hangOver(calender.getHangOver())
                    .contents(calender.getContents())
                    .drinks(calender.getDrinks())
                    .imageUrl(calender.getImageUrl().split(","))
                    .build();
        } catch (Throwable e) {
            throw new AlcException(CalenderError.NOT_FOUND_CALENDER);
        }
    }

    @Override
    public List<SearchCalenderModel> search(SearchCalenderCommand command) {
        try {
            List<Calender> calenders = userCalenderRepositoryImpl.search(command.getMonth(), command.getDay(), command.getUserId());
            return calenders.stream().map(calender -> new SearchCalenderModel(
                    calender.getId(),
                    calender.getTitle(),
                    calender.getDrinks().stream().max(Comparator.comparing(DrinksModel::getQuantity)).get().getType().name(),
                    calender.getDrinks().stream().mapToLong(DrinksModel::getQuantity).sum(),
                    calender.getDrinkStartTime().toString()
            )).toList();
        } catch (Throwable e) {
            throw new AlcException(CalenderError.NOT_FOUND_CALENDER);
        }
    }

    @Override
    @Transactional
    public void save(CreateCalenderCommand command) {
        try {
            Calender calender = Calender.builder()
                    .userId(command.getUserId())
                    .title((command.getTitle() == null) ? DEFAULT_TITLE : command.getTitle())
                    .drinks(command.getDrinks())
                    .hangOver(command.getHangOver())
                    .drinkStartTime(command.getDrinkStartTime())
                    .drinkEndTime((command.getDrinkEndTime() == null) ? Time.valueOf(LocalDateTime.now().toLocalTime()) : command.getDrinkEndTime())
                    .imageUrl(command.getImageUrl())
                    .contents(command.getContents())
                    .drinkReport(DrinkType.calculate(command.getDrinks()))
                    .build();
            calenderRepository.save(calender);
            saveUserCalenders(command, calender.getId());
        } catch (Throwable e) {
            throw new AlcException(CalenderError.COULD_NOT_SAVE);
        }
    }

    @Override
    @Transactional
    public void update(UpdateCalenderCommand command, Long calenderId) {
        Calender calender = calenderRepository.findById(calenderId).orElseThrow(() -> new AlcException(CalenderError.NOT_FOUND_CALENDER));
        try {
            calender.update(
                    command.getTitle(), command.getDrinks(),
                    command.getHangOver(), command.getDrinkStartTime(),
                    command.getDrinkEndTime(), command.getImageUrl(), command.getContents()
            );
        } catch (Throwable e) {
            throw new AlcException(CalenderError.COULD_NOT_SAVE);
        }
    }

    @Override
    @Transactional
    public void delete(Long calenderId, String userId) {
        try {
            calenderRepository.deleteCalenderById(calenderId);
        } catch (Exception exception) {
            throw new AlcException(CalenderError.DELETE_ERROR_CALENDER);
        }
    }


    private void saveUserCalenders(CreateCalenderCommand command, Long calenderId) {
        ArrayList<UserCalender> userCalenders = new ArrayList<>();
        userCalenders.add(UserCalender.builder()
                .editRole(EditRole.EDITOR)
                .userId(command.getUserId())
                .calenderId(calenderId)
                .build());

        List<UserCalender> viewer = Arrays.stream(command.getFriends())
                .map(friend -> UserCalender.builder()
                        .editRole(EditRole.VIEWER)
                        .userId(friend)
                        .calenderId(calenderId).build())
                .toList();
        userCalenders.addAll(viewer);

        userCalenderRepository.saveAll(userCalenders);
    }
}

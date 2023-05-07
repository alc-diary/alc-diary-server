package com.alc.diary.application.calender;

import com.alc.diary.application.calender.dto.request.SaveCalenderRequest;
import com.alc.diary.application.calender.dto.request.UpdateCalenderRequest;
import com.alc.diary.application.calender.dto.response.FindCalenderDetailResponse;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.error.CalenderError;
import com.alc.diary.domain.calender.model.CalenderImage;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import com.alc.diary.domain.exception.CalenderException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CalenderService {

    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    @Transactional(readOnly = true)
    public FindCalenderDetailResponse find(Long calenderId) {
        try {
            Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow();
            return FindCalenderDetailResponse.of(
                    calender.getTitle(), calender.getContents(),
                    calender.getDrinkStartDateTime(), calender.getDrinkEndDateTime(),
                    calender.getDrinkModels(), (calender.getImage() == null) ? List.of() : calender.getImage().getImages(),
                    calender.getDrinkCondition()
            );
        } catch (Throwable e) {
            throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
        }
    }

    @Transactional
    public void save(SaveCalenderRequest request, Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow();

            calenderRepository.save(
                    Calender.builder()
                            .title(request.title())
                            .contents(request.contents())
                            .drinkStartDateTime(request.drinkStartDateTime())
                            .drinkEndDateTime(request.drinkEndDateTime())
                            .drinkModels(request.drinkModels())
                            .image(new CalenderImage(request.images()))
                            .drinkCondition(request.drinkCondition())
                            .user(user)
                            .build()
            );
        } catch (Exception e) {
            throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
        }
    }

    @Transactional
    public void delete(Long calenderId, Long userId) {
        if (!isValidUser(calenderId, userId)) return;
        try {
            calenderRepository.deleteCalenderById(calenderId);
        } catch (Exception e) {
            throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
        }
    }

    @Transactional
    public void update(Long calenderId, Long userId, UpdateCalenderRequest request) {
        if (!isValidUser(calenderId, userId)) return;
        try {
            User user = userRepository.findById(userId).orElseThrow();
            Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow();
            calender.update(
                    request.title(), request.contents(),
                    request.drinkStartDateTime(), request.drinkEndDateTime(),
                    request.drinkModels(), new CalenderImage(request.images()),
                    request.drinkCondition(), user
            );
        } catch (Exception e) {
            throw new CalenderException(CalenderError.NO_ENTITY_FOUND);
        }
    }

    private Boolean isValidUser(Long calenderId, Long userId) {
        Calender calender = calenderRepository.getCalenderById(calenderId).orElseThrow();
        return (Objects.equals(calender.user.getId(), userId));
    }
}

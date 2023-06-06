package com.alc.diary.application.calender;

import com.alc.diary.application.calender.dto.request.SaveMainCalenderRequest;
import com.alc.diary.application.calender.dto.response.GetMainResponse;
import com.alc.diary.application.calender.dto.response.MainCalenderResponse;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.error.CalenderError;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import com.alc.diary.domain.calender.repository.CustomCalenderRepository;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MainCalenderService {

    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    private final CustomCalenderRepository customCalenderRepository;

    @Transactional
    public MainCalenderResponse saveMain(SaveMainCalenderRequest request, Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow();

            Calender calender = calenderRepository.save(Calender.Of(request.drinkStartDateTime(), request.drinkModels(), user));
            return new MainCalenderResponse(calender.getId());
        } catch (Throwable e) {
            throw new DomainException(CalenderError.NOT_VALID_USER);
        }
    }

    public GetMainResponse getMain(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow();
            long overAlcoholLimit = customCalenderRepository.countAlcoholLimit(userId);
            return GetMainResponse.create(user.getDetail().getNickname(), user.getDetail().getDescriptionStyle(), overAlcoholLimit, user.getDetail().getNonAlcoholGoal());
        } catch (Throwable e) {
            throw new DomainException(CalenderError.NO_ENTITY_FOUND);
        }
    }
}
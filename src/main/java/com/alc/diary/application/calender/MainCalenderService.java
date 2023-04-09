package com.alc.diary.application.calender;

import com.alc.diary.application.calender.dto.request.SaveMainCalenderRequest;
import com.alc.diary.domain.calender.Calender;
import com.alc.diary.domain.calender.repository.CalenderRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MainCalenderService {

    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    @Transactional
    public void saveMain(SaveMainCalenderRequest request, Long userId) {
        val title = "오늘의 음주기록";
        User user = userRepository.findById(userId).orElseThrow();

        calenderRepository.save(Calender.Of(title, request.drinkStartTime(), request.drinkModels(), user));
    }

    public void getMain(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        // TODO: user descriptionType과 goal 기반으로 멘트 리턴.
    }
}
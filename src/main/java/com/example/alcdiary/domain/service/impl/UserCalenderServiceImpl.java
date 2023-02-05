package com.example.alcdiary.domain.service.impl;

import com.example.alcdiary.domain.enums.EditRole;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.UserError;
import com.example.alcdiary.domain.service.UserCalenderService;
import com.example.alcdiary.infrastructure.entity.UserCalender;
import com.example.alcdiary.infrastructure.jpa.UserCalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCalenderServiceImpl implements UserCalenderService {


    private final UserCalenderRepository userCalenderRepository;

    @Override
    @Transactional
    public Boolean validateUserRole(String userId, Long calenderId) {
        UserCalender userCalender = userCalenderRepository.findUserCalenderByUserIdAndCalenderId(
                userId, calenderId).orElseThrow(() -> new AlcException(UserError.NO_AUTH_CALENDER_USER));
        return userCalender.getEditRole().equals(EditRole.EDITOR);
    }

}

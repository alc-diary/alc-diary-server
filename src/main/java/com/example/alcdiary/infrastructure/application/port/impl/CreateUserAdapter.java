package com.example.alcdiary.infrastructure.application.port.impl;

import com.example.alcdiary.application.port.CreateUserPort;
import com.example.alcdiary.domain.enums.SocialType;
import com.example.alcdiary.infrastructure.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class CreateUserAdapter implements CreateUserPort {

    @Override
    public User create(SocialType socialType, String token) {
        return null;
    }
}

package com.example.alcdiary.application.port;

import com.example.alcdiary.domain.enums.SocialType;
import com.example.alcdiary.infrastructure.entity.user.User;

public interface CreateUserPort {

    public User create(SocialType socialType, String token);
}

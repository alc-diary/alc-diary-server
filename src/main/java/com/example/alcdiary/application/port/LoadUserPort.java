package com.example.alcdiary.application.port;

import com.example.alcdiary.domain.enums.SocialType;
import com.example.alcdiary.domain.model.UserModel;

public interface LoadUserPort {

    UserModel load(SocialType socialType, String token);
}

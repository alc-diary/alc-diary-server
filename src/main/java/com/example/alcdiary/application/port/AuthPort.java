package com.example.alcdiary.application.port;

import com.example.alcdiary.domain.model.AuthModel;
import com.example.alcdiary.domain.model.UserModel;

public interface AuthPort {

    AuthPort service(UserModel.SocialType socialType);
    AuthPort token(String token);
    AuthModel authentication();
}

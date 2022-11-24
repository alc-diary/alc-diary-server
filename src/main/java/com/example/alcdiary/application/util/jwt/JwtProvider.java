package com.example.alcdiary.application.util.jwt;

import com.example.alcdiary.application.util.jwt.model.JwtModel;
import com.example.alcdiary.domain.model.user.UserIdModel;

public interface JwtProvider {

    String generateToken(UserIdModel userIdModel);

    boolean validateToken(String token);

    UserIdModel getKey(String bearerToken);
}

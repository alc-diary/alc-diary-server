package com.example.alcdiary.domain.util.jwt;

import com.example.alcdiary.domain.model.UserModel;
import io.jsonwebtoken.Claims;

public interface JwtProvider {

    String createToken(UserModel userModel);

    UserModel resolveToken(String token);
}

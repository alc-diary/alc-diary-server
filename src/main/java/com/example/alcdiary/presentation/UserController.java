package com.example.alcdiary.presentation;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final JwtProvider jwtProvider;

    @GetMapping
    public UserModel get(
            @RequestHeader("Authorization") String bearerToken
    ) {
        UserModel userModel = jwtProvider.resolveToken(bearerToken);

        return userModel;
    }
}

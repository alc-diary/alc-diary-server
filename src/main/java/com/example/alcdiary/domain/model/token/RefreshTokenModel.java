package com.example.alcdiary.domain.model.token;

import com.example.alcdiary.domain.model.UserModel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefreshTokenModel {

    private String token;
    private LocalDateTime expiredAt;
    private UserModel userModel;
}

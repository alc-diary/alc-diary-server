package com.example.alcdiary.infrastructure.entity.user;

import com.example.alcdiary.domain.model.UserModel;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserPk implements Serializable {

    private Long id;
    private UserModel.SocialType socialType;
}

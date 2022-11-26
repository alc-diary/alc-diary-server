package com.example.alcdiary.domain.model.user;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CommonError;
import lombok.Getter;

@Getter
public class UserIdModel {

    private long id;
    private EUserServiceType serviceType;

    private UserIdModel() {
    }

    public static UserIdModel from(String s) {
        UserIdModel userIdModel = new UserIdModel();

        userIdModel.id = Long.parseLong(s.substring(1));

        String serviceCode = s.substring(0, 1);
        if (serviceCode.equals("K")) {
            userIdModel.serviceType = EUserServiceType.KAKAO;
        } else if (serviceCode.equals("G")) {
            userIdModel.serviceType = EUserServiceType.GOOGLE;
        } else {
            throw new AlcException(CommonError.INVALID_PARAMETER);
        }

        return userIdModel;
    }

    public static UserIdModel of(long id, EUserServiceType serviceType) {
        UserIdModel userIdModel = new UserIdModel();
        userIdModel.id = id;
        userIdModel.serviceType = serviceType;
        return userIdModel;
    }

    public String parse() {
        return serviceType.getCode() + id;
    }
}

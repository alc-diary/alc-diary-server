package com.example.alcdiary.domain.model.user;

import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CommonError;
import lombok.Getter;

@Getter
public class UserIdModel {

    private long id;
    private EUserServiceType serviceType;

    public UserIdModel(long id, EUserServiceType serviceType) {
        this.id = id;
        this.serviceType = serviceType;
    }

    public String parse() {
        return serviceType.getCode() + id;
    }

    public static UserIdModel fromString(String s) {
        String code = s.substring(0, 1);
        long id = Long.parseLong(s.substring(1));
        if (code.equals("K")) {
            return new UserIdModel(id, EUserServiceType.KAKAO);
        }
        if (code.equals("G")) {
            return new UserIdModel(id, EUserServiceType.GOOGLE);
        }
        throw new AlcException(CommonError.INVALID_PARAMETER);
    }
}

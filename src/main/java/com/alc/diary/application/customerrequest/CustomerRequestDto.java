package com.alc.diary.application.customerrequest;

import com.alc.diary.domain.customerrequest.CustomerRequest;
import com.alc.diary.domain.customerrequest.ServiceSatisfactionLevel;

public record CustomerRequestDto(

        long id,
        long userId,
        ServiceSatisfactionLevel serviceSatisfactionLevel,
        String requestContent) {

    public static CustomerRequestDto fromDomain(CustomerRequest domain) {
        return new CustomerRequestDto(
                domain.getId(), domain.getUserId(), domain.getServiceSatisfactionLevel(), domain.getRequestContent());
    }
}

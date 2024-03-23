package com.alc.diary.application.admin.customerrequest;

import com.alc.diary.domain.customerrequest.CustomerRequest;
import com.alc.diary.domain.customerrequest.ServiceSatisfactionLevel;

import java.time.LocalDateTime;

public record AdminCustomerRequestDto(

        long id,
        long userId,
        ServiceSatisfactionLevel serviceSatisfactionLevel,
        String requestContent,
        LocalDateTime createdAt
) {

    public static AdminCustomerRequestDto fromDomain(CustomerRequest domain) {
        return new AdminCustomerRequestDto(
                domain.getId(),
                domain.getUserId(),
                domain.getServiceSatisfactionLevel(),
                domain.getRequestContent(),
                domain.getCreatedAt());
    }
}

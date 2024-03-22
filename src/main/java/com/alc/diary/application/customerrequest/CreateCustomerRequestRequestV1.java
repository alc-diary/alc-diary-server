package com.alc.diary.application.customerrequest;

import com.alc.diary.domain.customerrequest.ServiceSatisfactionLevel;

public record CreateCustomerRequestRequestV1(

        ServiceSatisfactionLevel serviceSatisfactionLevel,
        String requestContent
) {
}

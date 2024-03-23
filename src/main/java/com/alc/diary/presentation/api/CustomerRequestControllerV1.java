package com.alc.diary.presentation.api;

import com.alc.diary.application.customerrequest.CreateCustomerRequestRequestV1;
import com.alc.diary.application.customerrequest.CustomerRequestDto;
import com.alc.diary.application.customerrequest.CustomerRequestServiceV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/v1/customer-requests")
@RestController
public class CustomerRequestControllerV1 {

    private final CustomerRequestServiceV1 customerRequestService;

    @PostMapping
    public ApiResponse<CustomerRequestDto> create(
            @ApiIgnore @RequestAttribute long userId, @RequestBody CreateCustomerRequestRequestV1 request) {
        return ApiResponse.getCreated(customerRequestService.create(userId, request));
    }
}

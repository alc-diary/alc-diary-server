package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.customerrequest.AdminCustomerRequestDto;
import com.alc.diary.application.admin.customerrequest.AdminCustomerRequestServiceV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/customer-requests")
@RestController
public class AdminCustomerRequestControllerV1 {

    private final AdminCustomerRequestServiceV1 adminCustomerRequestService;

    @GetMapping
    public ApiResponse<Page<AdminCustomerRequestDto>> getAll(Pageable pageable) {
        return ApiResponse.getSuccess(adminCustomerRequestService.getAll(pageable));
    }

    @GetMapping("/{customerRequestId}")
    public ApiResponse<AdminCustomerRequestDto> getById(@PathVariable long customerRequestId) {
        return ApiResponse.getSuccess(adminCustomerRequestService.getById(customerRequestId));
    }
}

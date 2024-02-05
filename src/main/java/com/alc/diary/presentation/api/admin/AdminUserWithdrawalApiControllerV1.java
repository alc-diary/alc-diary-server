package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.user.AdminUserWithdrawalServiceV1;
import com.alc.diary.application.admin.user.UserWithdrawalDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/user-withdrawals")
@RestController
public class AdminUserWithdrawalApiControllerV1 {

    private final AdminUserWithdrawalServiceV1 adminUserWithdrawalServiceV1;

    @GetMapping
    public ApiResponse<Page<UserWithdrawalDto>> getAllUserWithdrawals(Pageable pageable) {
        return ApiResponse.getSuccess(adminUserWithdrawalServiceV1.getAllUserWithdrawals(pageable));
    }

    @GetMapping("/{userWithdrawalId}")
    public ApiResponse<UserWithdrawalDto> getUserWithdrawalById(@PathVariable long userWithdrawalId) {
        return ApiResponse.getSuccess(adminUserWithdrawalServiceV1.getUserWithdrawalById(userWithdrawalId));
    }
}

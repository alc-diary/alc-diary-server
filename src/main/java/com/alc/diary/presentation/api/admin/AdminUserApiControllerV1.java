package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.user.AdminUserService;
import com.alc.diary.application.admin.user.UserDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/users")
@RestController
public class AdminUserApiControllerV1 {

    private final AdminUserService adminUserService;

    @GetMapping
    public ApiResponse<Page<UserDto>> getAllUsers(Pageable pageable) {
        return ApiResponse.getSuccess(adminUserService.getAllUsers(pageable));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserDto> getUserById(@PathVariable long userId) {
        return ApiResponse.getSuccess(adminUserService.getUserById(userId));
    }
}

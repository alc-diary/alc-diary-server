package com.alc.diary.presentation;

import com.alc.diary.application.user.UserAppService;
import com.alc.diary.application.user.dto.response.GetUserInfoAppResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserAppService userAppService;

    @GetMapping("/info")
    public ApiResponse<GetUserInfoAppResponse> getUserInfo(
        @RequestAttribute Long userId
    ) {
        return ApiResponse.getSuccess(userAppService.getUser(userId));
    }
}

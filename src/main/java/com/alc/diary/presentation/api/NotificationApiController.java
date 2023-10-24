package com.alc.diary.presentation.api;

import com.alc.diary.application.notification.NotificationService;
import com.alc.diary.application.notification.dto.SaveFcmTokenRequest;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/v1/notifications")
@RestController
public class NotificationApiController {

    private final NotificationService notificationService;

    @PutMapping
    public ApiResponse<Void> saveFcmToken(
            @ApiIgnore @RequestAttribute long userId,
            @Validated @RequestBody SaveFcmTokenRequest request
    ) {
        notificationService.saveFcmToken(userId, request);
        return ApiResponse.getSuccess();
    }

    @PostMapping
    public ApiResponse<Void> sendFcm(
            @ApiIgnore @RequestAttribute long userId
    ) {
        notificationService.sendFcm(userId);
        return ApiResponse.getSuccess();
    }
}

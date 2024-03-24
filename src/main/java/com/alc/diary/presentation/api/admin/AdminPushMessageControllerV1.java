package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.pushmessage.AdminCreatePushMessageRequestV1;
import com.alc.diary.application.admin.pushmessage.AdminPushMessageDto;
import com.alc.diary.application.admin.pushmessage.AdminPushMessageServiceV1;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/push-messages")
@RestController
public class AdminPushMessageControllerV1 {

    private final AdminPushMessageServiceV1 adminPushMessageService;

    @PostMapping
    public ApiResponse<AdminPushMessageDto> create(@RequestBody AdminCreatePushMessageRequestV1 request) {
        return ApiResponse.getCreated(adminPushMessageService.create(request));
    }

    @GetMapping
    public ApiResponse<Page<AdminPushMessageDto>> getAll(Pageable pageable) {
        return ApiResponse.getSuccess(adminPushMessageService.getAll(pageable));
    }

    @GetMapping("/{pushMessageId}")
    public ApiResponse<AdminPushMessageDto> getById(@PathVariable long pushMessageId) {
        return ApiResponse.getSuccess(adminPushMessageService.getById(pushMessageId));
    }

    @PostMapping("/{pushMessageId}/users/send")
    public ApiResponse<Void> sendPushToAllUsers(@PathVariable long pushMessageId) {
        adminPushMessageService.sendPushToAllUsers(pushMessageId);
        return ApiResponse.getSuccess();
    }
}

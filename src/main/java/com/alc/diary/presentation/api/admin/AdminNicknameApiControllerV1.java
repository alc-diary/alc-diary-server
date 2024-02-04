package com.alc.diary.presentation.api.admin;

import com.alc.diary.application.admin.nickname.AdminNicknameService;
import com.alc.diary.application.admin.nickname.NicknameTokenDto;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/admin/v1/nicknames")
@RestController
public class AdminNicknameApiControllerV1 {

    private final AdminNicknameService adminNicknameService;

    @GetMapping("/tokens/first")
    public ApiResponse<Page<NicknameTokenDto>> getAllFirstNicknameTokens(Pageable pageable) {
        return ApiResponse.getSuccess(adminNicknameService.getAllFirstNicknameToken(pageable));
    }

    @GetMapping("/tokens/second")
    public ApiResponse<Page<NicknameTokenDto>> getAllSecondNicknameTokens(Pageable pageable) {
        return ApiResponse.getSuccess(adminNicknameService.getAllSecondNicknameToken(pageable));
    }
}

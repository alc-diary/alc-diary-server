package com.alc.diary.presentation.api;

import com.alc.diary.application.share.ShareService;
import com.alc.diary.application.share.dto.ShareCalenderDetailResponse;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class ShareController {

    private final ShareService shareService;

    @GetMapping(value = "/v1/share/{calenderId}")
    public ApiResponse<String> share(@PathVariable String calenderId,
                                     @ApiIgnore @RequestAttribute Long userId) {
        return ApiResponse.getSuccess(shareService.createShareLink(userId.toString(), calenderId));
    }

    @GetMapping(value = "/v1/link")
    public ApiResponse<ShareCalenderDetailResponse> getLinkData(@RequestParam String encoded) {
        return ApiResponse.getSuccess(shareService.getLinkCalender(encoded));
    }
}

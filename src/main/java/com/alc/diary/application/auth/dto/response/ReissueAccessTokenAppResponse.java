package com.alc.diary.application.auth.dto.response;

public record ReissueAccessTokenAppResponse(
    String newAccessToken,
    String newRefreshToken
) {
}

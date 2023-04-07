package com.alc.diary.application.auth.dto.request;

import com.alc.diary.domain.user.enums.SocialType;
import lombok.Getter;

public record SocialLoginAppRequest(SocialType socialType, String socialAccessToken) {
}

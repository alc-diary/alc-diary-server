package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;

public record CreateRandomNicknameTokenAppRequest(
    NicknameTokenOrdinal ordinal,
    String nicknameToken
) {
}

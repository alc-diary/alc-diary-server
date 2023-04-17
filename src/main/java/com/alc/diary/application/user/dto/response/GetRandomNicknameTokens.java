package com.alc.diary.application.user.dto.response;

import java.util.List;

public record GetRandomNicknameTokens(
        List<NicknameTokenDto> firstNicknameTokens,
        List<NicknameTokenDto> secondNicknameTokens
) {
    public record NicknameTokenDto(Long id, String token) {}
}

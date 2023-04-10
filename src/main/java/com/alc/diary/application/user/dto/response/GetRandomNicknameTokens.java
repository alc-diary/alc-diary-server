package com.alc.diary.application.user.dto.response;

import java.util.List;

public record GetRandomNicknameTokens(
        List<String> firstNicknameTokens,
        List<String> secondNicknameTokens
) {
}

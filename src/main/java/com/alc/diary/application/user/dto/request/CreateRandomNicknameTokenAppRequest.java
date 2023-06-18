package com.alc.diary.application.user.dto.request;

import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record CreateRandomNicknameTokenAppRequest(
    @NotNull(message = "순번은 필수입니다.") NicknameTokenOrdinal ordinal,
    @NotNull(message = "닉네임 토큰은 필수입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 한글, 영어 대소문자, 숫자만 가능합니다.")
    String nicknameToken
) {
}

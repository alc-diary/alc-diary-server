package com.alc.diary.application.user.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record UpdateNicknameAppRequest(

        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 한글, 영어 대소문자, 숫자만 가능합니다.")
        String newNickname
) {
}

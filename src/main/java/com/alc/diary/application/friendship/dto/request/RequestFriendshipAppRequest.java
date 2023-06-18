package com.alc.diary.application.friendship.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record RequestFriendshipAppRequest(

        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 한글, 영어 대소문자, 숫자만 가능합니다.")
        String targetNickname,
        String alias,
        @Size(max = 100, message = "메시지는 100자 이하여야 합니다.") String message
) {
}

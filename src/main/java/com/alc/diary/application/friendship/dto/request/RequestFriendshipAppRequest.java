package com.alc.diary.application.friendship.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record RequestFriendshipAppRequest(

        @NotNull(message = "유저 ID는 필수입니다.") Long targetUserId,
        @Size(max = 100, message = "메시지는 100자 이하여야 합니다.") String message
) {
}

package com.alc.diary.application.user.dto.request;

import javax.validation.constraints.Size;

public record DeactivateUserAppRequest(

        long targetUserId,
        @Size(max = 500, message = "탈퇴 사유는 500자 이하여야 합니다.") String reason
) {
}

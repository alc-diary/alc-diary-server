package com.alc.diary.application.user.dto.request;

import javax.validation.constraints.NotNull;

public record UpdateUserProfileImageAppRequest(

        @NotNull(message = "프로필 이미지 URL은 필수입니다.") String newProfileImage
) {
}

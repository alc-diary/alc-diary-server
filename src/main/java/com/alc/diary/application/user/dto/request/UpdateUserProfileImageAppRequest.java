package com.alc.diary.application.user.dto.request;

import javax.validation.constraints.Size;

public record UpdateUserProfileImageAppRequest(

        @Size(max = 500, message = "프로필 이미지 ULR은 500자 이하여야 합니다.") String newProfileImage
) {
}

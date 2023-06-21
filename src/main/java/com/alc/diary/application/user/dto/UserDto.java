package com.alc.diary.application.user.dto;

public record UserDto(

        long id,
        UserDetailDto detail,
        String profileImage
) {

    private record UserDetailDto(

            long id,
            long userId,
            String nickname
    ) {
    }
}

package com.alc.diary.application.friendship.dto.response;

public record GetFriendsAppResponse(

        long friendId,
        String nickname,
        String label,
        String profileImageUrl
) {

    public static GetFriendsAppResponse from() {

    }
}

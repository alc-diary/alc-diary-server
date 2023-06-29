package com.alc.diary.application.friendship.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record SendFriendRequestAppRequest(

        @NotNull(message = "User ID cannot be null.") long receiverId,
        @Size(max = 100, message = "Message length cannot exceed 100 characters.") String message
) {
}

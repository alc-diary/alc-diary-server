package com.alc.diary.application.friendship.dto.request;

import javax.validation.constraints.Size;

public record AcceptFriendRequestAppRequest(

        @Size(max = 30) String friendLabel
) {
}

package com.alc.diary.application.friendship.dto.request;

import java.util.List;

public record AcceptFriendshipRequestAppRequest(

        Long requestId,
        String alias
) {
}

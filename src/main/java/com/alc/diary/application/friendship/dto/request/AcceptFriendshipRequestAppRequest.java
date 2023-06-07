package com.alc.diary.application.friendship.dto.request;

import java.util.List;

public record AcceptFriendshipRequestAppRequest(

        List<Long> requestIds
) {
}

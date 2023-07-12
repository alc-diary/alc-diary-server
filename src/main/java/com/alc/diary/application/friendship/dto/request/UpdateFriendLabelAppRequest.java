package com.alc.diary.application.friendship.dto.request;

import javax.validation.constraints.Size;

public record UpdateFriendLabelAppRequest(

        @Size(max = 30) String newFriendLabel
) {
}

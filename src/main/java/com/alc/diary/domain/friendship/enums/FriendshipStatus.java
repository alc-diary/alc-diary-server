package com.alc.diary.domain.friendship.enums;

public enum FriendshipStatus {

    REQUESTED,
    ACCEPTED,
    DECLINED,
    DELETED,
    ;

    public boolean isRequestedOrAccepted() {
        return this == REQUESTED || this == ACCEPTED;
    }
}

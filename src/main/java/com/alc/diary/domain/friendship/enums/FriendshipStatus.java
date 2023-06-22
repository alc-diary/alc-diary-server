package com.alc.diary.domain.friendship.enums;

public enum FriendshipStatus {

    REQUESTED,
    ACCEPTED,
    DECLINED,
    DELETED,
    ;

    public boolean isRequestedOrAccepted() {
        return isRequested() || isAccepted();
    }

    public boolean isRequested() {
        return this == REQUESTED;
    }

    public boolean isAccepted() {
        return this == ACCEPTED;
    }
}

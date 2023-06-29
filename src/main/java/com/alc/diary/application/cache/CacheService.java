package com.alc.diary.application.cache;

public interface CacheService {

    void setUnreadFriendRequestBadge(long userId);

    boolean hasUnreadFriendRequest(long userId);

    void clearUnreadFriendRequestBadge(long userId);
}

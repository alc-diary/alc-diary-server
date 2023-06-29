package com.alc.diary.application.cache;

public interface CacheService {

    void markFriendRequestAsUnread(long userId);

    boolean hasUnreadFriendRequest(long userId);

    void markFriendRequestAsRead(long userId);
}

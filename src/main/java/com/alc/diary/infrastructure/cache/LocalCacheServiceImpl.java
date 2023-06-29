package com.alc.diary.infrastructure.cache;

import com.alc.diary.application.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Profile("local")
@Component
public class LocalCacheServiceImpl implements CacheService {

    @Override
    public void markFriendRequestAsUnread(long userId) {
        System.out.println("local");
    }

    @Override
    public boolean hasUnreadFriendRequest(long userId) {
        return false;
    }

    @Override
    public void markFriendRequestAsRead(long userId) {

    }
}

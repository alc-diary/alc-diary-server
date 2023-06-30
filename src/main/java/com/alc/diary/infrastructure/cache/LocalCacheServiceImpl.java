package com.alc.diary.infrastructure.cache;

import com.alc.diary.application.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Profile("local")
@Component
public class LocalCacheServiceImpl implements CacheService {

    private final Map<Long, Boolean> friendRequestBadgeStatus = new ConcurrentHashMap<>();

    @Override
    public void setUnreadFriendRequestBadge(long userId) {
        friendRequestBadgeStatus.put(userId, true);
    }

    @Override
    public boolean hasUnreadFriendRequest(long userId) {
        return friendRequestBadgeStatus.getOrDefault(userId, false);
    }

    @Override
    public void clearUnreadFriendRequestBadge(long userId) {
        friendRequestBadgeStatus.put(userId, false);
    }
}

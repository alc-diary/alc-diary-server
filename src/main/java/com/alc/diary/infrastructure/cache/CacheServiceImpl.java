package com.alc.diary.infrastructure.cache;

import com.alc.diary.application.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Profile("prod")
@Component
public class CacheServiceImpl implements CacheService {

    private static final String UNREAD_FRIEND_REQUESTS_KEY = "unread_friend_requests";
    private final StringRedisTemplate redisTemplate;

    @Override
    public void markFriendRequestAsUnread(long userId) {
        System.out.println("prod");
        redisTemplate.opsForValue().setBit(UNREAD_FRIEND_REQUESTS_KEY, userId, true);
    }

    @Override
    public boolean hasUnreadFriendRequest(long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(UNREAD_FRIEND_REQUESTS_KEY, userId));
    }

    @Override
    public void markFriendRequestAsRead(long userId) {
        redisTemplate.opsForValue().setBit(UNREAD_FRIEND_REQUESTS_KEY, userId, false);
    }
}

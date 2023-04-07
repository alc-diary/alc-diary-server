package com.alc.diary.domain.auth.policy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DefaultExpiredPolicy implements TokenExpiredPolicy {
    @Override
    public LocalDateTime calculate(Clock currentTime) {
        Instant instant = Instant.now(currentTime);
        ZoneId zoneId = currentTime.getZone();
        return LocalDateTime.ofInstant(instant, zoneId)
            .plusHours(1L);
    }
}

package com.alc.diary.domain.auth.policy;

import java.time.Clock;
import java.time.LocalDateTime;

@FunctionalInterface
public interface TokenExpiredPolicy {

    LocalDateTime calculate(Clock clock);
}

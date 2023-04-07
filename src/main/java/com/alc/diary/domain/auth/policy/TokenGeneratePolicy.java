package com.alc.diary.domain.auth.policy;

@FunctionalInterface
public interface TokenGeneratePolicy {

    String generate();
}

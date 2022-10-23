package com.example.alcdiary.infrastructure.domain.util.uuid.impl;

import com.example.alcdiary.domain.util.uuid.UUIDProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProviderImpl implements UUIDProvider {

    @Override
    public String createUUID() {
        return UUID.randomUUID().toString();
    }
}

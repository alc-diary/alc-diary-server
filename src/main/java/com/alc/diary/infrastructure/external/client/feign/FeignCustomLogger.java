package com.alc.diary.infrastructure.external.client.feign;

import feign.Logger;

public class FeignCustomLogger extends Logger {
    @Override
    protected void log(String configKey, String format, Object... args) {

    }
}

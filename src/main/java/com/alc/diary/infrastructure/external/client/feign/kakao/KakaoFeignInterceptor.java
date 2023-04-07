package com.alc.diary.infrastructure.external.client.feign.kakao;

import feign.Request.HttpMethod;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class KakaoFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        if (template.method().equals(HttpMethod.GET.name())) {
            log.info("[GET] queries: {}", template.queries());
            return;
        }

        String encodedRequestBody = StringUtils.toEncodedString(template.body(), StandardCharsets.UTF_8);
        log.info("[POST] requestBody: {}", encodedRequestBody);

        String convertRequestBody = encodedRequestBody;
        template.body(convertRequestBody);
    }
}

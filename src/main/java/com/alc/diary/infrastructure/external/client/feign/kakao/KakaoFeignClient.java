package com.alc.diary.infrastructure.external.client.feign.kakao;

import com.alc.diary.infrastructure.external.client.feign.kakao.dto.response.KakaoLoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "kakao-client",
        url = "https://kapi.kakao.com",
        configuration = KakaoFeignConfig.class
)
public interface KakaoFeignClient {

    @GetMapping("/v2/user/me")
    ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestHeader("Authorization") String kakaoBearerToken);
}

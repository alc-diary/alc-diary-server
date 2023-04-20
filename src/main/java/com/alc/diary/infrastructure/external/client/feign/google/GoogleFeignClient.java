package com.alc.diary.infrastructure.external.client.feign.google;

import com.alc.diary.infrastructure.external.client.feign.google.dto.response.GoogleUserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "google-client",
        url = "https://www.googleapis.com"
)
public interface GoogleFeignClient {

    @GetMapping("/oauth2/v2/userinfo")
    ResponseEntity<GoogleUserInfoDto> getUserInfo(@RequestHeader("Authorization") String googleBearerToken);
}

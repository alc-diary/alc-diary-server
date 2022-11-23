package com.example.alcdiary.infrastructure.kakao;

import com.example.alcdiary.application.kakao.KakaoAuth;
import com.example.alcdiary.application.kakao.model.KakaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class KakaoAuthImpl implements KakaoAuth {

    private final String kakaoHost = "https://kapi.kakao.com";

    @Override
    public KakaoResponse getUserInfo(String bearerToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", bearerToken);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        UriComponents uri = UriComponentsBuilder
                .fromUriString(kakaoHost + "/v2/user/me")
                .build(false);

        ResponseEntity<KakaoResponse> response =
                restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, KakaoResponse.class);

        return response.getBody();
    }
}

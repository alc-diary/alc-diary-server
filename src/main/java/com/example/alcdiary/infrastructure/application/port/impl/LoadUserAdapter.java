package com.example.alcdiary.infrastructure.application.port.impl;

import com.example.alcdiary.application.port.LoadUserPort;
import com.example.alcdiary.domain.enums.SocialType;
import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.infrastructure.adapter.KakaoUserResponse;
import com.example.alcdiary.infrastructure.entity.user.User;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
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
public class LoadUserAdapter implements LoadUserPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserModel load(SocialType socialType, String token) {
        switch (socialType) {
            case KAKAO:
                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

                UriComponents uri = UriComponentsBuilder
                        .fromUriString("https://kapi.kakao.com/v2/user/me")
                        .build(false);

                ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity, KakaoUserResponse.class);

                assert response.getBody() != null;
                User findUser = userJpaRepository.findById("K" + response.getBody().getId()).orElseGet(() -> {
                    User user = User.builder()
                            .id("K" + response.getBody().getId())
                            .email(response.getBody().getKakao_account().getEmail())
                            .profileImageUrl(response.getBody().getKakao_account().getProfile().getProfile_image_url())
                            .build();
                    return userJpaRepository.save(user);
                });

                return findUser.convertToDomainModel();
            case GOOGLE:
                System.out.println("google");
                return null;
            default:
                return null;
        }
    }
}

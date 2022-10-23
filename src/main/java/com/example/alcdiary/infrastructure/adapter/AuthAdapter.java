package com.example.alcdiary.infrastructure.adapter;

import com.example.alcdiary.application.port.AuthPort;
import com.example.alcdiary.domain.model.AuthModel;
import com.example.alcdiary.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class AuthAdapter implements AuthPort {

    private UserModel.SocialType socialType;
    private String token;

    @Override
    public AuthPort service(UserModel.SocialType socialType) {
        this.socialType = socialType;
        return this;
    }

    @Override
    public AuthPort token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public AuthModel authentication() {
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
                return AuthModel.builder()
                        .socialType(socialType)
                        .id(response.getBody().getId())
                        .email(response.getBody().getKakao_account().getEmail())
                        .profileImageUrl(
                                response.
                                        getBody()
                                        .getKakao_account()
                                        .getProfile()
                                        .getProfile_image_url()).build();
            case GOOGLE:
                System.out.println("google");
                break;
            default:
        }
        return null;
    }
}

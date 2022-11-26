package com.example.alcdiary.application.kakao;

import com.example.alcdiary.application.kakao.model.KakaoResponse;

public interface KakaoAuth {

    KakaoResponse getUserInfo(String token);
}

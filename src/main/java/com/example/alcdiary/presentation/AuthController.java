package com.example.alcdiary.presentation;

import com.example.alcdiary.application.LoginUseCase;
import com.example.alcdiary.application.ReissueAccessTokenUseCase;
import com.example.alcdiary.application.result.LoginResult;
import com.example.alcdiary.application.result.ReissueAccessTokenResult;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CommonError;
import com.example.alcdiary.presentation.dto.ResponseDto;
import com.example.alcdiary.presentation.dto.request.KakaoLoginRequest;
import com.example.alcdiary.presentation.dto.response.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final ReissueAccessTokenUseCase reissueAccessTokenUseCase;

    @PostMapping("/kakao")
    public ResponseEntity<ResponseDto<KakaoLoginResponse>> kakaoLogin(
            @RequestHeader("Authorization") String bearerToken
    ) {
        LoginResult loginResult = loginUseCase.execute(bearerToken);
        KakaoLoginResponse response = KakaoLoginResponse.from(loginResult);
        return new ResponseDto<>(response)
                .toResponseEntity();
    }

    @PostMapping("/reissue/access-token")
    public ResponseEntity<ResponseDto<ReissueAccessTokenResult>> reissueAccessToken(
            @RequestHeader("Authorization") String bearerToken
    ) {
        return ResponseEntity.ok()
                .body(new ResponseDto<>(reissueAccessTokenUseCase.execute(bearerToken)));
    }
}

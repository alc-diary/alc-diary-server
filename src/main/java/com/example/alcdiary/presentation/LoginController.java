package com.example.alcdiary.presentation;

import com.example.alcdiary.application.LoginUseCase;
import com.example.alcdiary.application.result.LoginResult;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.CommonError;
import com.example.alcdiary.presentation.dto.ResponseDto;
import com.example.alcdiary.presentation.dto.request.KakaoLoginRequest;
import com.example.alcdiary.presentation.dto.response.KakaoLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/login")
@RequiredArgsConstructor
@RestController
public class LoginController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/kakao")
    public ResponseEntity<ResponseDto<KakaoLoginResponse>> kakaoLogin(
            @RequestBody KakaoLoginRequest request
    ) {
        LoginResult loginResult = loginUseCase.execute(request.toLoginCommand());
        KakaoLoginResponse response = KakaoLoginResponse.from(loginResult);
        return new ResponseDto<>(response)
                .toResponseEntity();
    }

    @GetMapping
    public void test() {
        throw new AlcException(CommonError.INVALID_PARAMETER);
    }
}

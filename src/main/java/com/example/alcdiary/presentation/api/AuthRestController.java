package com.example.alcdiary.presentation.api;

import com.example.alcdiary.application.KakaoLoginUseCase;
import com.example.alcdiary.application.ReIssueAccessTokenUseCase;
import com.example.alcdiary.application.command.KakaoLoginCommand;
import com.example.alcdiary.application.command.ReIssueAccessTokenCommand;
import com.example.alcdiary.application.result.KakaoLoginResult;
import com.example.alcdiary.application.result.ReIssueAccessTokenResult;
import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.model.user.UserIdModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthRestController {

    private final ReIssueAccessTokenUseCase reIssueAccessTokenUseCase;
    private final KakaoLoginUseCase kakaoLoginUseCase;
    private final JwtProvider jwtProvider;

    @PostMapping ("/access-token/re-issue")
    public ReIssueAccessTokenResult reIssueAccessToken(
            @RequestHeader("Authorization") String bearerToken
    ) {
        ReIssueAccessTokenCommand command = new ReIssueAccessTokenCommand(bearerToken);
        return reIssueAccessTokenUseCase.execute(command);
    }

    @PostMapping("/token/kakao")
    public KakaoLoginResult kakaoLogin(
            @RequestHeader("Authorization") String bearerToken
    ) {
        KakaoLoginCommand command = new KakaoLoginCommand(bearerToken);
        return kakaoLoginUseCase.execute(command);
    }

    @GetMapping("/test")
    public UserIdModel test(
            @RequestHeader("Authorization") String bearerToken
    ) {
        UserIdModel userIdModel = jwtProvider.getKey(bearerToken.substring("Bearer ".length()));
        return userIdModel;
    }
}

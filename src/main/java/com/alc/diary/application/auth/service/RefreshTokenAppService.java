package com.alc.diary.application.auth.service;

import com.alc.diary.application.auth.dto.request.ReissueAccessTokenAppRequest;
import com.alc.diary.application.auth.dto.response.ReissueAccessTokenAppResponse;
import com.alc.diary.domain.auth.RefreshToken;
import com.alc.diary.domain.auth.error.AuthError;
import com.alc.diary.domain.auth.repository.RefreshTokenRepository;
import com.alc.diary.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class RefreshTokenAppService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public ReissueAccessTokenAppResponse reissueToken(ReissueAccessTokenAppRequest request) {
        RefreshToken oldRefreshToken = refreshTokenRepository.findByToken(request.refreshToken())
            .orElseThrow(() -> new DomainException(AuthError.INVALID_REFRESH_TOKEN));
        if (oldRefreshToken.isExpired()) {
            throw new DomainException(AuthError.EXPIRED_REFRESH_TOKEN);
        }
        oldRefreshToken.expire();

        String newAccessToken = jwtService.generateToken(oldRefreshToken.getUser().getId());
        RefreshToken newRefreshToken = RefreshToken.getDefault(oldRefreshToken.getUser());
        RefreshToken savedRefreshToken = refreshTokenRepository.save(newRefreshToken);
        return new ReissueAccessTokenAppResponse(newAccessToken, savedRefreshToken.getToken());
    }
}

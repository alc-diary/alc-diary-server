package com.alc.diary.presentation.filter;

import com.alc.diary.application.auth.service.JwtService;
import com.alc.diary.domain.auth.error.AuthError;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Order(Integer.MIN_VALUE)
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final String[] whiteList = new String[]{"/v1/auth", "/h2-console", "/swagger-ui", "/swagger-resources", "/v3/api-docs", "/kakao", "/admin", "/css", "/assets", "/test", "/temp", "/v1/admin"};
    private final String[] equalsList = new String[]{"/favicon.ico", "/swagger-ui/index.html", "/v2/api-docs"};
    private final String ONBOARDING_API_PATTERN = "/v\\d+/onboarding.*";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (StringUtils.equals(path, "/")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (StringUtils.startsWithAny(path, whiteList)
            || StringUtils.equalsAny(path, equalsList)
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = getAccessToken(request);
            if (!jwtService.validateToken(accessToken)) {
                throw new DomainException(AuthError.INVALID_ACCESS_TOKEN);
            }
            long userId = jwtService.getUserIdFromToken(accessToken);

            // TODO: 온보딩 관련 처리 작업 해야함.
//            if (isOnboardingEndpoint(path)) {
//                if (path.equals("/v1/onboarding/is-onboarding-done")) {
//                    if (!userRepository.existsNotDeactivatedUserById(userId)) {
//                        throw new DomainException(UserError.USER_NOT_FOUND);
//                    }
//                } else if (!userRepository.existsOnboardingUserById(userId)) {
//                    throw new DomainException(UserError.USER_NOT_FOUND);
//                }
//            } else if (path.startsWith("/v1/user-status")) {
//                if (!userRepository.existsNotDeactivatedUserById(userId)) {
//                    throw new DomainException(UserError.USER_NOT_FOUND);
//                }
//            } else {
//                if (!userRepository.existsActiveUserById(userId)) {
//                    throw new DomainException(UserError.USER_NOT_FOUND);
//                }
//            }

            request.setAttribute("userId", userId);
            filterChain.doFilter(request, response);
        } catch (DomainException e) {
            // TODO: 추후에 예외 처리 상세하게 분리
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private static String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER_NAME);
        if (bearerToken == null) {
            throw new DomainException(AuthError.NO_AUTHORIZATION_HEADER);
        }
        if (!bearerToken.startsWith("Bearer ")) {
            throw new DomainException(AuthError.INVALID_BEARER_TOKEN_FORMAT);
        }
        return bearerToken.substring("Bearer ".length());
    }

//    private boolean isOnboardingEndpoint(String path) {
//        return path.matches(ONBOARDING_API_PATTERN);
//    }
}

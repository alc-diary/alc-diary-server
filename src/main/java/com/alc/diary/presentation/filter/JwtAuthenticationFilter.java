package com.alc.diary.presentation.filter;

import com.alc.diary.application.auth.service.JwtService;
import com.alc.diary.domain.auth.error.AuthError;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@Component
@Order(Integer.MIN_VALUE)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final String[] whiteList = new String[]{"/api/v1/auth", "/h2-console", "/swagger-ui", "/swagger-resources", "/v3/api-docs", "/kakao"};
    private final String[] equalsList = new String[]{"/favicon.ico"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (
                StringUtils.startsWithAny(path, whiteList)
                        || StringUtils.equalsAny(path, equalsList)
        ) {
            log.info("Request - Method: {}, URL: {}", request.getMethod(), path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = getAccessToken(request);
            jwtService.validateToken(accessToken);
            long userId = jwtService.getUserIdFromToken(accessToken);
            long findUserId = userRepository.findById(userId)
                    .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND))
                    .getId();
            if (!userRepository.findById(userId).isPresent()) {
                throw new DomainException(UserError.USER_NOT_FOUND);
            }
            request.setAttribute("userId", findUserId);
            log.info("Request - Method:{}, URL: {}, Access Token: {}, User Id: {}", request.getMethod(), path, request.getHeader("Authorization"), findUserId);
            filterChain.doFilter(request, response);
        } catch (DomainException e) {
            log.info("Request - Method:{}, URL: {}, Access Token: {}", request.getMethod(), path, request.getHeader("Authorization"));
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            log.info("Request - Method:{}, URL: {}, Access Token: {}", request.getMethod(), path, request.getHeader("Authorization"));
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
        String accessToken = bearerToken.substring("Bearer ".length());
        return accessToken;
    }
}

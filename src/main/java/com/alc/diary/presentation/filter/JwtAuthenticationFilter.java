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

    private final String[] whiteList = new String[]{"/api/v1/auth", "/h2-console", "/swagger-ui/", "/swagger-resources", "/v3/api-docs"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (StringUtils.startsWithAny(path, whiteList)) {
            log.info("end-point: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String bearerToken = request.getHeader(AUTH_HEADER_NAME);
            if (bearerToken == null) {
                throw new DomainException(AuthError.NO_AUTHORIZATION_HEADER);
            }
            String accessToken = bearerToken.substring("Bearer ".length());
            if (!jwtService.validateToken(accessToken)) {
                throw new DomainException(AuthError.EXPIRED_ACCESS_TOKEN);
            }
            long userId = jwtService.getUserIdFromToken(accessToken);
            long findUserId = userRepository.findById(userId)
                    .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND))
                    .getId();
            if (!userRepository.findById(userId).isPresent()) {
                throw new DomainException(UserError.USER_NOT_FOUND);
            }
            request.setAttribute("userId", findUserId);
            log.info("end-point: {}, userId: {}", path, findUserId);
            filterChain.doFilter(request, response);
        } catch (DomainException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}

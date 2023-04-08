package com.alc.diary.presentation.filter;

import com.alc.diary.application.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
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
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println(path);
        if (
            path.startsWith("/api/v1/auth")
            || path.startsWith("/h2-console")
        ) {
            log.info("end-point: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader(AUTH_HEADER_NAME);
        String accessToken = bearerToken.substring("Bearer ".length());
        if (!jwtService.validateToken(accessToken)) {
            handlerExceptionResolver.resolveException(request, response, null, new IllegalArgumentException("test"));
            return;
        }
        long userId = jwtService.getUserIdFromToken(accessToken);
        request.setAttribute("userId", userId);
        log.info("end-point: {}, userId: {}", path, userId);
        filterChain.doFilter(request, response);
    }
}

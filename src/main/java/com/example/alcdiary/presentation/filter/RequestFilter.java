package com.example.alcdiary.presentation.filter;

import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final Logger log = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        log.info(request.getRequestURI());
        if (path.equals("/") || path.startsWith("/auth") || path.startsWith("/admin") || path.equals("/favicon.ico") || path.startsWith("/static")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");
        if (!jwtProvider.validateToken(bearerToken)) {
            throw new AlcException(AuthError.EXPIRED_ACCESS_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}

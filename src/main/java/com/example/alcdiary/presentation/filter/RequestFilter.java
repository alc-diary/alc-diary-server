package com.example.alcdiary.presentation.filter;

import com.example.alcdiary.application.util.jwt.JwtProvider;
import com.example.alcdiary.domain.exception.AlcException;
import com.example.alcdiary.domain.exception.error.AuthError;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println(path);
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");
        if (!jwtProvider.validateToken(bearerToken.substring("Bearer ".length()))) {
            throw new AlcException(AuthError.EXPIRED_ACCESS_TOKEN);
        }

        filterChain.doFilter(request, response);
    }
}

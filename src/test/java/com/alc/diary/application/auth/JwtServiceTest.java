package com.alc.diary.application.auth;

import com.alc.diary.application.auth.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final Instant CURRENT_TIME = Instant.parse("2023-04-01T00:30:00+09:00");

    private static final String VALID_TOKEN = // expired at 2023-04-01T01:00:00+09:00
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjgwMjc0ODAwLCJleHAiOjE2ODAyNzg0MDB9.yLWdyoHZBe4KYSJ7iEek8A72Vt4K-B2LlDALficBl70";

    private static final String INVALID_TOKEN = // expired at 2023-04-01T00:29:59+09:00
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjgwMjcyOTk5LCJleHAiOjE2ODAyNzY1OTl9.be8K23Ajyoh1ig3vnMlm9l-14UUSukTg69uhR0cIZQk";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(
                "server-secret",
                CURRENT_TIME
        );
    }

    @Test
    void jwt_test() {
        String jwt = jwtService.generateToken(1234567890);
        assertThat(jwt)
                .isEqualTo("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNjgwMjc2NjAwLCJleHAiOjE2ODAyODAyMDB9.yt06QpgM7RFoZbFSBxvV89An7iuthHLXzGrb17jlF6k");
    }

    @Test
    void test() {
        String jwt = VALID_TOKEN;
        assertThat(jwtService.getUserIdFromToken(jwt))
                .isEqualTo(1234567890);
    }

    @Test
    void validateToken() {
        String jwt = VALID_TOKEN;
        assertThat(jwtService.validateToken(jwt))
                .isTrue();
    }

    @Test
    void validateToken_exception() {
        String jwt = INVALID_TOKEN;
        assertThat(jwtService.validateToken(jwt))
                .isFalse();
    }
}
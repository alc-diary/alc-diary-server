package com.alc.diary.application.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserAppServiceTest {
    private UserAppService userAppService;

    @BeforeEach
    void setUp() {
        this.userAppService = new UserAppService(null, null);
    }

    @Test
    void 겹치지않는_닉네임을_생성한다() {

    }
}
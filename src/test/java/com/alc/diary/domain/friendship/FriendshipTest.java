package com.alc.diary.domain.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class FriendshipTest {

    private Friendship friendship;

    @BeforeEach
    void setUp() {
        friendship = Friendship.create(1L, "hello", 2L, "world");
    }

    @CsvSource(value = {"1, hello", "2, world"})
    @ParameterizedTest
    void getFriendUserLabel(long userId, String label) {
        assertThat(friendship.getFriendUserLabel(userId)).isEqualTo(label);
    }

    @CsvSource(value = {"1, 2", "2, 1"})
    @ParameterizedTest
    void getFriendUserIdTest(long userAId, long userBId) {
        assertThat(friendship.getFriendUserId(userAId)).isEqualTo(userBId);
    }
}
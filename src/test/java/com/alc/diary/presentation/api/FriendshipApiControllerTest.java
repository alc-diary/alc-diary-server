package com.alc.diary.presentation.api;

import com.alc.diary.application.friendship.FriendshipAppService;
import com.alc.diary.application.friendship.dto.request.SendFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.UpdateFriendLabelAppRequest;
import com.alc.diary.application.friendship.dto.response.GetFriendListAppResponse;
import com.alc.diary.application.friendship.dto.response.GetPendingFriendRequestsAppResponse;
import com.alc.diary.application.friendship.dto.response.SearchUserWithFriendStatusByNicknameAppResponse;
import com.alc.diary.presentation.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.alc.diary.application.friendship.dto.response.SearchUserWithFriendStatusByNicknameAppResponse.FriendStatus.NOT_SENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        value = FriendshipApiController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
class FriendshipApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendshipAppService friendshipAppService;

    @DisplayName("유저 ID를 받아 친구 목록을 응답한다.")
    @Test
    void shouldReturnFriendListWhenUserIdGiven() throws Exception {
        // given
        long userId = 1L;
        given(friendshipAppService.getFriendList(userId))
                .willReturn(List.of(
                        new GetFriendListAppResponse(
                                3L,
                                2L,
                                3L,
                                "friendNickname1",
                                "friend label1",
                                "friend profile image1"
                        ),
                        new GetFriendListAppResponse(
                                4L,
                                3L,
                                4L,
                                "friendNickname2",
                                "friend label2",
                                "friend profile image2"
                        )));

        // when & then
        ResultActions perform = mvc.perform(get("/v1/friendships")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId));

        assertSuccess(perform)
                .andExpect(jsonPath("$.data[0].friendId").value(3L))
                .andExpect(jsonPath("$.data[0].friendshipId").value(2L))
                .andExpect(jsonPath("$.data[0].friendUserId").value(3L))
                .andExpect(jsonPath("$.data[0].friendNickname").value("friendNickname1"))
                .andExpect(jsonPath("$.data[0].friendLabel").value("friend label1"))
                .andExpect(jsonPath("$.data[0].friendProfileImageUrl").value("friend profile image1"))

                .andExpect(jsonPath("$.data[1].friendId").value(4L))
                .andExpect(jsonPath("$.data[1].friendshipId").value(3L))
                .andExpect(jsonPath("$.data[1].friendUserId").value(4L))
                .andExpect(jsonPath("$.data[1].friendNickname").value("friendNickname2"))
                .andExpect(jsonPath("$.data[1].friendLabel").value("friend label2"))
                .andExpect(jsonPath("$.data[1].friendProfileImageUrl").value("friend profile image2"));
        then(friendshipAppService).should().getFriendList(userId);
    }

    @DisplayName("친구 별칭을 업데이트 하면 success로 응답한다.")
    @Test
    void shouldReturnSuccessWhenUpdatingFriendLabel() throws Exception {
        // given
        long friendshipId = 1L;
        long userId = 1L;
        UpdateFriendLabelAppRequest request = new UpdateFriendLabelAppRequest("new friend label");

        // when & then
        ResultActions perform = mvc.perform(put("/v1/friendships/" + friendshipId + "/friend-label")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsBytes(request)));
        assertSuccess(perform);
        then(friendshipAppService).should().updateFriendLabel(userId, friendshipId, request);
    }

    @DisplayName("친구를 삭제하면 success로 응답한다.")
    @Test
    void shouldReturnSuccessWhenDeletingFriendship() throws Exception {
        // given
        long userId = 1L;
        long friendshipId = 1L;

        // when & then
        ResultActions perform = mvc.perform(delete("/v1/friendships/" + friendshipId)
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId));
        assertSuccess(perform);
        then(friendshipAppService).should().deleteFriend(userId, friendshipId);
    }

    @DisplayName("닉네임으로 친구를 검색하면 유저 정보를 응답한다.")
    @Test
    void shouldReturnUserInfoWhenSearchingByNickname() throws Exception {
        // given
        long userId = 1L;
        String nickname = "test";
        SearchUserWithFriendStatusByNicknameAppResponse response =
                new SearchUserWithFriendStatusByNicknameAppResponse(2L, "profile image url", nickname, NOT_SENT);
        given(friendshipAppService.searchUserWithFriendStatusByNickname(userId, nickname))
                .willReturn(response);

        // when & then
        ResultActions perform = mvc.perform(get("/v1/friendships/search-user-with-friendship-status")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .param("nickname", nickname));

        assertSuccess(perform)
                .andExpect(jsonPath("$.data.userId").value(2L))
                .andExpect(jsonPath("$.data.profileImageUrl").value("profile image url"))
                .andExpect(jsonPath("$.data.nickname").value(nickname))
                .andExpect(jsonPath("$.data.status").value("NOT_SENT"));

        then(friendshipAppService).should().searchUserWithFriendStatusByNickname(userId, nickname);
    }

    @DisplayName("친구 요청이 정상적으로 보내지면 CREATED를 응답한다.")
    @Test
    void shouldReturnCreatedWhenSendFriendRequest() throws Exception {
        // given
        long userId = 1L;
        long receiverId = 2L;
        String message = "message";
        SendFriendRequestAppRequest request = new SendFriendRequestAppRequest(receiverId, message);

        ResultActions perform = mvc.perform(post("/v1/friendships/request")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId)
                .content(objectMapper.writeValueAsBytes(request)));
        assertCreated(perform);

        // when & then
        then(friendshipAppService).should().sendFriendRequest(userId, request);
    }

    @DisplayName("유저 ID를 받아 수락되지 않은 친구 요청들을 응답한다.")
    @Test
    void shouldReturnPendingFriendshipRequestsWhenUserIdGiven() throws Exception {
        // given
        long userId = 10L;
        given(friendshipAppService.getPendingFriendRequests(userId))
                .willReturn(
                        List.of(
                                new GetPendingFriendRequestsAppResponse(1L, 1L, "nickname1", "url1"),
                                new GetPendingFriendRequestsAppResponse(2L, 2L, "nickname2", "url2"),
                                new GetPendingFriendRequestsAppResponse(3L, 3L, "nickname3", "url3")
                        ));

        // when & then
        ResultActions perform = mvc.perform(get("/v1/friendships/request/pending")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", userId));
        assertSuccess(perform)
                .andExpect(jsonPath("$.data[0].friendshipId").value(1L))
                .andExpect(jsonPath("$.data[0].receiverId").value(1L))
                .andExpect(jsonPath("$.data[0].receiverNickname").value("nickname1"))
                .andExpect(jsonPath("$.data[0].receiverProfileImageUrl").value("url1"))

                .andExpect(jsonPath("$.data[1].friendshipId").value(2L))
                .andExpect(jsonPath("$.data[1].receiverId").value(2L))
                .andExpect(jsonPath("$.data[1].receiverNickname").value("nickname2"))
                .andExpect(jsonPath("$.data[1].receiverProfileImageUrl").value("url2"))

                .andExpect(jsonPath("$.data[2].friendshipId").value(3L))
                .andExpect(jsonPath("$.data[2].receiverId").value(3L))
                .andExpect(jsonPath("$.data[2].receiverNickname").value("nickname3"))
                .andExpect(jsonPath("$.data[2].receiverProfileImageUrl").value("url3"));

        then(friendshipAppService).should().getPendingFriendRequests(userId);
    }

    private ResultActions assertSuccess(ResultActions perform) throws Exception {
        return perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("S0000"))
                .andExpect(jsonPath("$.message").value("success"));
    }

    private ResultActions assertCreated(ResultActions perform) throws Exception {
        return perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.code").value("S0001"))
                .andExpect(jsonPath("$.message").value("created"));
    }
}

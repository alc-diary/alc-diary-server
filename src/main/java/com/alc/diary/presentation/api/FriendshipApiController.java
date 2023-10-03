package com.alc.diary.presentation.api;

import com.alc.diary.application.friendship.FriendshipAppService;
import com.alc.diary.application.friendship.dto.request.AcceptFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.SendFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.UpdateFriendLabelAppRequest;
import com.alc.diary.application.friendship.dto.response.*;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.UserDetail;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.presentation.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/friendships")
@RestController
public class FriendshipApiController {

    private final FriendshipAppService friendshipAppService;

    /**
     * 친구 목록 조회
     *
     * @param userId
     * @return
     */
    @GetMapping
    public ApiResponse<List<GetFriendListAppResponse>> getFriendList(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getFriendList(userId));
    }

    /**
     * 친구 별칭 수정
     *
     * @param userId
     * @param friendshipId
     * @param request
     * @return
     */
    @PutMapping("/{friendshipId}/friend-label")
    public ApiResponse<Void> updateFriendLabel(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId,
            @Validated @RequestBody UpdateFriendLabelAppRequest request
    ) {
        friendshipAppService.updateFriendLabel(userId, friendshipId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 친구 삭제
     *
     * @param requesterId
     * @param friendshipId
     * @return
     */
    @DeleteMapping("/{friendshipId}")
    public ApiResponse<Void> deleteFriend(
            @ApiIgnore @RequestAttribute(name = "userId") long requesterId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.deleteFriend(requesterId, friendshipId);
        return ApiResponse.getSuccess();
    }

    /**
     * 친구 요청을 보낼 유저를 닉네임으로 검색
     *
     * @param userId
     * @param nickname
     * @return
     */
    @GetMapping("/search-user-with-friendship-status")
    public ApiResponse<SearchUserWithFriendStatusByNicknameAppResponse> searchUserWithFriendStatusByNickname(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @RequestParam String nickname
    ) {
        if (nickname == null || !UserDetail.NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new DomainException(UserError.INVALID_NICKNAME_FORMAT);
        }
        return ApiResponse.getSuccess(
                friendshipAppService.searchUserWithFriendStatusByNickname(userId, nickname)
        );
    }

    /**
     * 친구 요청 전송
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/request")
    public ApiResponse<Void> sendFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @Validated @RequestBody SendFriendRequestAppRequest request
    ) {
        friendshipAppService.sendFriendRequest(userId, request);
        return ApiResponse.getCreated();
    }

    /**
     * 수락되지 않은 친구 요청 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/request/pending")
    public ApiResponse<List<GetPendingFriendRequestsAppResponse>> getPendingFriendRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getPendingFriendRequests(userId));
    }

    /**
     * 나에게 온 친구 요청 목록 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/request/received")
    public ApiResponse<List<GetReceivedFriendRequestsAppResponse>> getReceivedFriendRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getReceivedFriendRequests(userId));
    }

    /**
     * 친구 요청 수락
     *
     * @param userId
     * @param friendRequestId
     * @param request
     * @return
     */
    @PutMapping("/request/{friendRequestId}/accept")
    public ApiResponse<Void> acceptFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendRequestId,
            @Validated @RequestBody AcceptFriendRequestAppRequest request
            ) {
        friendshipAppService.acceptFriendRequest(userId, friendRequestId, request);
        return ApiResponse.getSuccess();
    }

    /**
     * 친구 요청 거절
     *
     * @param userId
     * @param friendRequestId
     * @return
     */
    @PutMapping("/request/{friendRequestId}/reject")
    public ApiResponse<Void> rejectFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendRequestId
    ) {
        friendshipAppService.rejectFriendRequest(userId, friendRequestId);
        return ApiResponse.getSuccess();
    }

    /**
     * 내가 보낸 친구 요청 취소
     *
     * @param userId
     * @param friendRequestId
     * @return
     */
    @PutMapping("/request/{friendRequestId}/cancel")
    public ApiResponse<Void> cancelFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendRequestId
    ) {
        friendshipAppService.cancelFriendRequest(userId, friendRequestId);
        return ApiResponse.getSuccess();
    }

    /**
     * 읽지 않은 친구 요청이 있는지 조회
     *
     * @param userId
     * @return
     */
    @GetMapping("/request/has-unread")
    public ApiResponse<Boolean> hasUnreadFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.hasUnreadFriendRequest(userId));
    }

    /**
     * 새로운 친구 요청 라벨 제거
     *
     * @param userId
     * @return
     */
    @PutMapping("/request/clear-unread-badge")
    public ApiResponse<Void> clearUnreadFriendRequestBadge(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        friendshipAppService.clearUnreadFriendRequestBadge(userId);
        return ApiResponse.getSuccess();
    }
}

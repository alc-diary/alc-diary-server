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

    @PostMapping
    public ApiResponse<Void> sendFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @Validated @RequestBody SendFriendRequestAppRequest request
    ) {
        friendshipAppService.sendFriendRequest(userId, request);
        return ApiResponse.getCreated();
    }

    @GetMapping
    public ApiResponse<List<GetFriendListAppResponse>> getFriendList(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getFriendList(userId));
    }

    @DeleteMapping("/{friendshipId}")
    public ApiResponse<Void> deleteFriend(
            @ApiIgnore @RequestAttribute(name = "userId") long requesterId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.deleteFriend(requesterId, friendshipId);
        return ApiResponse.getSuccess();
    }

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

    @GetMapping("/pending-requests")
    public ApiResponse<List<GetPendingFriendRequestsAppResponse>> getPendingFriendRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getPendingFriendRequests(userId));
    }

    @GetMapping("/received-requests")
    public ApiResponse<List<GetReceivedFriendRequestsAppResponse>> getReceivedFriendRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getReceivedFriendRequests(userId));
    }

    @PutMapping("/{friendshipId}/accept-request")
    public ApiResponse<Void> acceptFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId,
            @RequestBody AcceptFriendRequestAppRequest request
            ) {
        friendshipAppService.acceptFriendRequest(userId, friendshipId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/{friendshipId}/reject-request")
    public ApiResponse<Void> rejectFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.rejectFriendRequest(userId, friendshipId);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/{friendshipId}/cancel-request")
    public ApiResponse<Void> cancelFriendRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.cancelFriendRequest(userId, friendshipId);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/{friendshipId}/friend-label")
    public ApiResponse<Void> updateFriendLabel(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId,
            @RequestBody UpdateFriendLabelAppRequest request
    ) {
        friendshipAppService.updateFriendLabel(userId, friendshipId, request);
        return ApiResponse.getSuccess();
    }
}

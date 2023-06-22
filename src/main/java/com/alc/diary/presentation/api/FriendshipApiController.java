package com.alc.diary.presentation.api;

import com.alc.diary.application.calendar.dto.response.SearchUserWithFriendshipStatusByNicknameAppResponse;
import com.alc.diary.application.friendship.FriendshipAppService;
import com.alc.diary.application.friendship.dto.request.RequestFriendshipAppRequest;
import com.alc.diary.application.friendship.dto.request.UpdateFriendshipAliasAppRequest;
import com.alc.diary.application.friendship.dto.response.GetFriendshipsAppResponse;
import com.alc.diary.application.friendship.dto.response.GetPendingRequestsAppResponse;
import com.alc.diary.application.friendship.dto.response.GetReceivedFriendshipRequestsAppResponse;
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
    public ApiResponse<Void> requestFriendship(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @Validated @RequestBody RequestFriendshipAppRequest request
    ) {
        friendshipAppService.requestFriendship(userId, request);
        return ApiResponse.getCreated();
    }

    @GetMapping
    public ApiResponse<List<GetFriendshipsAppResponse>> getFriendShips(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getFriendships(userId));
    }

    @GetMapping("/search-user-with-friendship-status")
    public ApiResponse<SearchUserWithFriendshipStatusByNicknameAppResponse> searchUserWithFriendshipStatusByNickname(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @RequestParam String nickname
    ) {
        if (nickname == null || !UserDetail.NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new DomainException(UserError.INVALID_NICKNAME_FORMAT);
        }
        return ApiResponse.getSuccess(
                friendshipAppService.searchUserWithFriendshipStatusByNickname(userId, nickname)
        );
    }

    @GetMapping("/pending-requests")
    public ApiResponse<List<GetPendingRequestsAppResponse>> getPendingRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getPendingRequests(userId));
    }

    @GetMapping("/received-requests")
    public ApiResponse<List<GetReceivedFriendshipRequestsAppResponse>> getReceivedFriendshipRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getReceivedFriendshipRequests(userId));
    }

    @PutMapping("/{friendshipId}/accept-request")
    public ApiResponse<Void> acceptFriendshipRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.acceptFriendshipRequest(userId, friendshipId);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/{friendshipId}/decline-request")
    public ApiResponse<Void> declineFriendshipRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.declineFriendshipRequest(userId, friendshipId);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/{friendshipId}/alias")
    public ApiResponse<Void> updateFriendshipAlias(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId,
            @RequestBody UpdateFriendshipAliasAppRequest request
    ) {
        friendshipAppService.updateFriendshipAlias(userId, friendshipId, request);
        return ApiResponse.getSuccess();
    }

    @DeleteMapping("/{friendshipId}")
    public ApiResponse<Void> deleteFriendship(
            @ApiIgnore @RequestAttribute(name = "userId") long requesterId,
            @PathVariable long friendshipId
    ) {
        friendshipAppService.deleteFriendship(requesterId, friendshipId);
        return ApiResponse.getSuccess();
    }
}

package com.alc.diary.presentation.api;

import com.alc.diary.application.friendship.FriendshipAppService;
import com.alc.diary.application.friendship.dto.request.AcceptFriendshipRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.DeclineFriendshipRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.RequestFriendshipAppRequest;
import com.alc.diary.application.friendship.dto.response.GetReceivedFriendshipRequestsAppResponse;
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

    @GetMapping("/received-requests")
    public ApiResponse<List<GetReceivedFriendshipRequestsAppResponse>> getReceivedFriendshipRequests(
            @ApiIgnore @RequestAttribute(name = "userId") long userId
    ) {
        return ApiResponse.getSuccess(friendshipAppService.getReceivedFriendshipRequests(userId));
    }

    @PutMapping("/{friendshipId}/accept-request")
    public ApiResponse<Void> acceptFriendshipRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @PathVariable long friendshipId,
            @RequestBody AcceptFriendshipRequestAppRequest request
    ) {
        friendshipAppService.acceptFriendshipRequest(userId, friendshipId, request);
        return ApiResponse.getSuccess();
    }

    @PutMapping("/decline-requests")
    public ApiResponse<Void> declineFriendshipRequest(
            @ApiIgnore @RequestAttribute(name = "userId") long userId,
            @RequestBody DeclineFriendshipRequestAppRequest request
    ) {
        friendshipAppService.declineFriendshipRequest(userId, request);
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

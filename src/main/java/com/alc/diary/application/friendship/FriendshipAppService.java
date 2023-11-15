package com.alc.diary.application.friendship;

import com.alc.diary.application.cache.CacheService;
import com.alc.diary.application.friendship.dto.request.AcceptFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.SendFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.UpdateFriendLabelAppRequest;
import com.alc.diary.application.friendship.dto.response.*;
import com.alc.diary.application.notification.NotificationService;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.FriendRequest;
import com.alc.diary.domain.friendship.error.FriendshipError;
import com.alc.diary.domain.friendship.error.FriendRequestError;
import com.alc.diary.domain.friendship.repository.FriendshipRepository;
import com.alc.diary.domain.friendship.repository.FriendRequestRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.alc.diary.application.friendship.dto.response.SearchUserWithFriendStatusByNicknameAppResponse.FriendStatus.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FriendshipAppService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final CacheService cacheService;
    private final NotificationService notificationService;

    /**
     * 친구 요청 보내기
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void sendFriendRequest(long userId, SendFriendRequestAppRequest request) {
        User sender = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
        User receiver = userRepository.findActiveUserById(request.receiverId())
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));

        validFriendRequest(sender.getId(), receiver.getId());

        FriendRequest friendRequestToSave = FriendRequest.create(sender.getId(), receiver.getId(), request.message());
        friendRequestRepository.save(friendRequestToSave);
        cacheService.setUnreadFriendRequestBadge(receiver.getId());

        try {
            notificationService.sendFcm(receiver.getId(), "술렁술렁", "친구 요청이 왔어!\n누가 보냈는지 확인해볼까?", "FRIEND_REQUEST");
        } catch (Exception e) {
            log.error("push exception: ", e);
        }
    }

    private void validFriendRequest(long userAId, long userBId) {
        if (userAId == userBId) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        if (friendRequestRepository.findPendingOrAcceptedRequestWithUsers(userAId, userBId).isPresent()) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        validFriendshipCountLimit(userAId, userBId);
    }

    /**
     * 현재 사용자의 친구 목록 조회
     *
     * @param userId
     * @return
     */
    public List<GetFriendListAppResponse> getFriendList(long userId) {
        List<Friendship> friendships = friendshipRepository.findByUserId(userId);
        List<Long> friendUserIds = friendships.stream()
                .map(friendship -> friendship.getFriendUserId(userId))
                .toList();

        Map<Long, User> userByUserId = getUserByIdIn(friendUserIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return friendships.stream()
                .map(friendship -> {
                    long friendUserId = friendship.getFriendUserId(userId);
                    User friendUser = userByUserId.get(friendUserId);
                    return new GetFriendListAppResponse(
                            friendship.getId(),
                            friendship.getId(),
                            friendUser.getId(),
                            friendUser.getNickname(),
                            friendship.getFriendUserLabel(userId),
                            friendUser.getProfileImage()
                    );
                })
                .toList();
    }

    private List<User> getUserByIdIn(List<Long> userIds) {
        if (Collections.isEmpty(userIds)) {
            return List.of();
        }
        return userRepository.findActiveUsersByIdIn(userIds);
    }

    /**
     * 닉네임으로 유저 정보와 친구 상태 조회
     *
     * @param userId
     * @param nickname
     * @return
     */
    public SearchUserWithFriendStatusByNicknameAppResponse searchUserWithFriendStatusByNickname(
            long userId,
            String nickname
    ) {
        return userRepository.findActiveUserByNickname(nickname).map(friend -> {
            if (friend.getId().equals(userId)) {
                throw new DomainException(FriendshipError.SELF_SEARCH);
            }
            if (friendRequestRepository.findPendingRequestWithUsers(userId, friend.getId()).isPresent()) {
                return new SearchUserWithFriendStatusByNicknameAppResponse(
                        friend.getId(),
                        friend.getProfileImage(),
                        friend.getDetail().getNickname(),
                        PENDING
                );
            }
            if (friendshipRepository.findWithUsers(userId, friend.getId()).isPresent()) {
                return new SearchUserWithFriendStatusByNicknameAppResponse(
                        friend.getId(),
                        friend.getProfileImage(),
                        friend.getDetail().getNickname(),
                        FRIENDS
                );
            }
            return new SearchUserWithFriendStatusByNicknameAppResponse(
                    friend.getId(),
                    friend.getProfileImage(),
                    friend.getDetail().getNickname(),
                    NOT_SENT
            );
        }).orElse(null);
    }

    /**
     * 현재 사용자가 받은 친구 요청 리스트를 조회
     *
     * @param userId
     * @return
     */
    public List<GetReceivedFriendRequestsAppResponse> getReceivedFriendRequests(long userId) {
        List<FriendRequest> receivedRequests = friendRequestRepository.findPendingRequestsByReceiverId(userId);
        List<Long> senderIds = receivedRequests.stream()
                .map(FriendRequest::getSenderId)
                .toList();
        Map<Long, User> friendByUserId = getUserByIdIn(senderIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return receivedRequests.stream()
                .filter(friendRequest -> friendByUserId.containsKey(friendRequest.getSenderId()))
                .map(friendRequest -> {
                    User friend = friendByUserId.get(friendRequest.getSenderId());
                    return new GetReceivedFriendRequestsAppResponse(
                            friendRequest.getId(),
                            friend.getId(),
                            friend.getNickname(),
                            friend.getProfileImage(),
                            friendRequest.getMessage()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * 수락 대기중인 친구 요청 목록 조회
     *
     * @param userId
     * @return
     */
    public List<GetPendingFriendRequestsAppResponse> getPendingFriendRequests(long userId) {
        List<FriendRequest> sendRequests = friendRequestRepository.findPendingRequestsBySenderId(userId);
        List<Long> receiverIds = sendRequests.stream()
                .map(FriendRequest::getReceiverId)
                .toList();
        Map<Long, User> receiverByUserId = getUserByIdIn(receiverIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return sendRequests.stream()
                .filter(friendRequest -> receiverByUserId.containsKey(friendRequest.getReceiverId()))
                .map(friendRequest -> {
                    User receiver = receiverByUserId.get(friendRequest.getReceiverId());
                    return new GetPendingFriendRequestsAppResponse(
                            friendRequest.getId(),
                            receiver.getId(),
                            receiver.getNickname(),
                            receiver.getProfileImage()
                    );
                })
                .toList();
    }

    /**
     * 친구 요청 수락
     *
     * @param userId       요청 유저 ID
     * @param friendRequestId 친구 데이터 ID
     */
    @Transactional
    public void acceptFriendRequest(long userId, long friendRequestId, AcceptFriendRequestAppRequest request) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId).orElseThrow();

        validFriendshipCountLimit(friendRequest.getSenderId(), friendRequest.getReceiverId());

        friendRequest.markAccepted(userId);

        Friendship friendshipToSave = Friendship.create(
                friendRequest.getSenderId(),
                null,
                friendRequest.getReceiverId(),
                request.friendLabel()
        );
        friendshipRepository.save(friendshipToSave);

        User receiver = userRepository.findActiveUserById(friendRequest.getReceiverId())
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));

        try {
            notificationService.sendFcm(
                    friendRequest.getSenderId(),
                    "술렁술렁",
                    receiver.getNickname() + "이 친구를 수락했어.",
                    "FRIEND_ACCEPTED");
        } catch (Exception e) {
            log.error("push exception: ", e);
        }
    }

    private void validFriendshipCountLimit(long userAId, long userBId) {
        if (friendshipRepository.countByUserId(userAId) >= 100) {
            throw new DomainException(FriendRequestError.FRIENDSHIP_LIMIT_EXCEEDED);
        }
        if (friendshipRepository.countByUserId(userBId) >= 100) {
            throw new DomainException(FriendRequestError.FRIENDSHIP_LIMIT_EXCEEDED);
        }
    }

    /**
     * 친구 삭제 (soft delete)
     *
     * @param userId  요청 유저 ID
     * @param friendshipId 삭제할 친구 데이터 ID
     */
    @Transactional
    public void deleteFriend(long userId, long friendshipId) {
        Friendship foundFriendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new DomainException(FriendshipError.FRIENDSHIP_NOT_FOUND));

        FriendRequest foundFriendRequest =
                friendRequestRepository.findAcceptedRequestWithUsers(foundFriendship.getUserAId(), foundFriendship.getUserBId())
                        .orElseThrow(() -> new DomainException(FriendRequestError.FRIEND_REQUEST_NOT_FOUND));
        foundFriendRequest.markFriendshipEnded(userId);
        friendshipRepository.deleteById(foundFriendship.getId());
    }

    /**
     * 친구 요청 거절
     *
     * @param userId       요청 유저 ID
     * @param friendRequestId 친구 데이터 ID
     */
    @Transactional
    public void rejectFriendRequest(long userId, long friendRequestId) {
        FriendRequest foundRequest = friendRequestRepository.findById(friendRequestId).orElseThrow();
        foundRequest.markRejected(userId);
    }

    /**
     * 보낸 친구 요청 취소하기
     *
     * @param userId
     * @param friendRequestId
     */
    @Transactional
    public void cancelFriendRequest(long userId, long friendRequestId) {
        FriendRequest foundRequest = friendRequestRepository.findById(friendRequestId).orElseThrow();
        foundRequest.markCanceled(userId);
    }

    /**
     * 친구 별칭 수정하기
     *
     * @param userId
     * @param friendshipId
     */
    @Transactional
    public void updateFriendLabel(long userId, long friendshipId, UpdateFriendLabelAppRequest request) {
        Friendship foundFriendship = friendshipRepository.findById(friendshipId).orElseThrow();
        foundFriendship.updateFriendLabel(userId, request.newFriendLabel());
    }

    public boolean hasUnreadFriendRequest(long userId) {
        return cacheService.hasUnreadFriendRequest(userId);
    }

    public void clearUnreadFriendRequestBadge(long userId) {
        cacheService.clearUnreadFriendRequestBadge(userId);
    }
}

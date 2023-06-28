package com.alc.diary.application.friendship;

import com.alc.diary.application.friendship.dto.request.AcceptFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.SendFriendRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.UpdateFriendLabelAppRequest;
import com.alc.diary.application.friendship.dto.response.*;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.FriendRequest;
import com.alc.diary.domain.friendship.error.FriendRequestError;
import com.alc.diary.domain.friendship.repository.FriendshipRepository;
import com.alc.diary.domain.friendship.repository.FriendRequestRepository;
import com.alc.diary.domain.user.User;
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

    /**
     * 친구 요청 보내기
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void sendFriendRequest(long userId, SendFriendRequestAppRequest request) {
        User sender = userRepository.findActiveUserById(userId).orElseThrow();
        User receiver = userRepository.findActiveUserById(request.receiverId()).orElseThrow();

        validFriendRequest(sender.getId(), receiver.getId());

        FriendRequest friendRequestToSave = FriendRequest.create(sender.getId(), receiver.getId(), request.message());
        friendRequestRepository.save(friendRequestToSave);
    }

    private void validFriendRequest(long userAId, long userBId) {
        if (userAId == userBId) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
        if (friendRequestRepository.findPendingOrAcceptedRequestWithUsers(userAId, userBId).isPresent()) {
            throw new DomainException(FriendRequestError.INVALID_REQUEST);
        }
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
     * 닉네임으로 유저 정보와 친구 상태 조회 v2
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
     * @param friendshipId 친구 데이터 ID
     */
    @Transactional
    public void acceptFriendRequest(long userId, long friendshipId, AcceptFriendRequestAppRequest request) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendshipId).orElseThrow();
        friendRequest.markAccepted(userId);

        Friendship friendshipToSave = Friendship.create(
                friendRequest.getSenderId(),
                null,
                friendRequest.getReceiverId(),
                request.friendLabel()
        );
        friendshipRepository.save(friendshipToSave);
    }

    /**
     * 친구 삭제 (soft delete)
     *
     * @param userId  요청 유저 ID
     * @param friendshipId 삭제할 친구 데이터 ID
     */
    @Transactional
    public void deleteFriend(long userId, long friendshipId) {
        Friendship foundFriendship = friendshipRepository.findById(friendshipId).orElseThrow();

        FriendRequest foundFriendRequest =
                friendRequestRepository.findAcceptedRequestWithUsers(foundFriendship.getUserAId(), foundFriendship.getUserBId())
                        .orElseThrow();
        foundFriendRequest.markFriendshipEnded();
        friendshipRepository.deleteById(foundFriendship.getId());
    }

    /**
     * 친구 요청 거절
     *
     * @param userId       요청 유저 ID
     * @param friendshipId 친구 데이터 ID
     */
    @Transactional
    public void rejectFriendRequest(long userId, long friendshipId) {
        FriendRequest foundRequest = friendRequestRepository.findById(friendshipId).orElseThrow();
        foundRequest.markRejected(userId);
    }

    /**
     * 보낸 친구 요청 취소하기
     *
     * @param userId
     * @param friendshipId
     */
    @Transactional
    public void cancelFriendRequest(long userId, long friendshipId) {
        FriendRequest foundRequest = friendRequestRepository.findById(friendshipId).orElseThrow();
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
}

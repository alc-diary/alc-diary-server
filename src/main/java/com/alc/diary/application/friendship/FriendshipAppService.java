package com.alc.diary.application.friendship;

import com.alc.diary.application.calendar.dto.response.SearchUserWithFriendshipStatusByNicknameAppResponse;
import com.alc.diary.application.friendship.dto.request.AcceptFriendshipRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.RequestFriendAppRequest;
import com.alc.diary.application.friendship.dto.request.RequestFriendshipAppRequest;
import com.alc.diary.application.friendship.dto.request.UpdateFriendshipAliasAppRequest;
import com.alc.diary.application.friendship.dto.response.*;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.Friend;
import com.alc.diary.domain.friendship.FriendRequest;
import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import com.alc.diary.domain.friendship.error.FriendshipError;
import com.alc.diary.domain.friendship.repository.FriendRepository;
import com.alc.diary.domain.friendship.repository.FriendRequestRepository;
import com.alc.diary.domain.friendship.repository.FriendshipRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FriendshipAppService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;

    /**
     * 친구 요청 보내기
     *
     * @param userId  요청 유저 ID
     * @param request request
     */
    @Transactional
    public void requestFriendship(long userId, RequestFriendshipAppRequest request) {
        User requester = getUserById(userId);
        User targetUser = getUserById(request.targetUserId());

        validFriendshipRequest(requester, targetUser);

        Friendship friendshipToSave =
                Friendship.createRequest(requester, targetUser, request.message());
        friendshipRepository.save(friendshipToSave);
    }

    /**
     * 친구 요청 보내기 v2
     *
     * @param userId
     * @param request
     */
    @Transactional
    public void requestFriend(long userId, RequestFriendAppRequest request) {
        User sender = getUserById(userId);
        User receiver = getUserById(request.receiverId());

        // request validation

        FriendRequest friendRequestToSave = FriendRequest.create(sender.getId(), receiver.getId(), request.message());
        friendRequestRepository.save(friendRequestToSave);
    }

    private User getUserById(long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId));
    }

    private void validFriendshipRequest(User requester, User targetUser) {
        if (requester.equals(targetUser)) {
            throw new DomainException(
                    FriendshipError.INVALID_REQUEST,
                    String.format("Request UserID: %d, Target User Id: %d", requester.getId(), targetUser.getId())
            );
        }

        if (isRequestAlreadySent(requester.getId(), targetUser.getId())) {
            throw new DomainException(FriendshipError.ALREADY_SENT_REQUEST);
        }
        if (areUsersFriends(requester.getId(), targetUser.getId())) {
            throw new DomainException(FriendshipError.ALREADY_FRIENDS);
        }
    }

    /**
     * 현재 사용자의 친구 목록 조회
     *
     * @param requesterId 요청 유저 ID
     * @return
     */
    public List<GetFriendshipsAppResponse> getFriendships(long requesterId) {
        return GetFriendshipsAppResponse.of(
                filterFriendshipWithActiveUsers(friendshipRepository.findAcceptedFriendshipsByUserId(requesterId)), requesterId
        );
    }

    /**
     * 현재 사용자의 친구 목록 조회 v2
     *
     * @param userId
     * @return
     */
    public List<GetFriendsAppResponse> getFriends(long userId) {
        List<Friend> friends = friendRepository.findByUserId(userId);
        List<Long> friendUserIds = friends.stream()
                .map(friend -> friend.getFriendUserId(userId))
                .toList();

        Map<Long, User> userByUserId = getUserByIdIn(friendUserIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return friends.stream()
                .map(friend -> {
                    long friendUserId = friend.getFriendUserId(userId);
                    User friendUser = userByUserId.get(friendUserId);
                    return new GetFriendsAppResponse(
                            friend.getId(),
                            friendUser.getNickname(),
                            friend.getFriendUserLabel(userId),
                            friendUser.getProfileImage()
                    );
                })
                .toList();
    }

    private List<User> getUserByIdIn(List<Long> userIds) {
        return userRepository.findActiveUsersByIdIn(userIds);
    }

    /**
     * 닉네임으로 유저 정보와 친구 상태를 조회
     *
     * @param requesterId
     * @param nickname
     */
    public SearchUserWithFriendshipStatusByNicknameAppResponse searchUserWithFriendshipStatusByNickname(
            long requesterId,
            String nickname
    ) {
        return userRepository.findActiveUserByNickname(nickname).map(foundUser -> {
                    FriendshipStatus status = null;

                    if (isRequestAlreadySent(requesterId, foundUser.getId())) {
                        status = FriendshipStatus.REQUESTED;
                    } else if (areUsersFriends(requesterId, foundUser.getId())) {
                        status = FriendshipStatus.ACCEPTED;
                    }

                    return SearchUserWithFriendshipStatusByNicknameAppResponse.of(foundUser, status);
                })
                .orElse(null);
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
                        "PENDING"
                );
            }
            if (friendRepository.findWithUsers(userId, friend.getId()).isPresent()) {
                return new SearchUserWithFriendStatusByNicknameAppResponse(
                        friend.getId(),
                        friend.getProfileImage(),
                        friend.getDetail().getNickname(),
                        "ACTIVE"
                );
            }
            return new SearchUserWithFriendStatusByNicknameAppResponse(
                    friend.getId(),
                    friend.getProfileImage(),
                    friend.getDetail().getNickname(),
                    "NO_FRIEND"
            );
        }).orElse(null);
    }

    private boolean isRequestAlreadySent(long user1Id, long user2Id) {
        return friendshipRepository.findRequestedFriendshipBetweenUsers(user1Id, user2Id).isPresent();
    }

    private boolean areUsersFriends(long user1Id, long user2Id) {
        return friendshipRepository.findAcceptedFriendshipBetweenUsers(user1Id, user2Id).isPresent();
    }

    /**
     * 현재 사용자가 받은 친구 요청 리스트를 조회
     *
     * @param userId 요청 유저 ID
     * @return 받은 친구 요청 리스트
     */
    public List<GetReceivedFriendshipRequestsAppResponse> getReceivedFriendshipRequests(long userId) {
        List<Friendship> foundFriendships = filterFriendshipWithActiveUsers(
                friendshipRepository.findByToUser_IdAndStatusEquals(userId, FriendshipStatus.REQUESTED)
        );
        return foundFriendships.stream()
                .map(GetReceivedFriendshipRequestsAppResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 현재 사용자가 받은 친구 요청 리스트를 조회 v2
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
     * 수락 대기중인 친구 요청 목록 조회 v2
     *
     * @param userId
     * @return
     */
    public List<GetPendingRequestsAppResponse> getPendingRequests(long userId) {
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
                    return new GetPendingRequestsAppResponse(
                            friendRequest.getId(),
                            receiver.getId(),
                            receiver.getNickname(),
                            receiver.getProfileImage()
                    );
                })
                .toList();
    }

    private List<Friendship> filterFriendshipWithActiveUsers(List<Friendship> friendships) {
        return friendships.stream()
                .filter(Friendship::areBothUserActive)
                .collect(Collectors.toList());
    }

    /**
     * 친구 요청 수락
     *
     * @param userId       요청 유저 ID
     * @param friendshipId 친구 데이터 ID
     */
    @Transactional
    public void acceptFriendshipRequest(long userId, long friendshipId, AcceptFriendshipRequestAppRequest request) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendshipId).orElseThrow();
        friendRequest.accept(userId);

        Friend friendToSave = Friend.create(
                friendRequest.getSenderId(),
                null,
                friendRequest.getReceiverId(),
                request.friendLabel(),
                friendRequest.getId()
        );
        friendRepository.save(friendToSave);
    }

    /**
     * 친구 삭제 (soft delete)
     *
     * @param requesterId  요청 유저 ID
     * @param friendshipId 삭제할 친구 데이터 ID
     */
    @Transactional
    public void deleteFriendship(long requesterId, long friendshipId) {
        Friend friend = friendRepository.findById(friendshipId).orElseThrow();




        Friendship foundFriendship = getFriendshipsById(friendshipId);
        foundFriendship.delete(requesterId);
    }

    /**
     * 친구 요청 거절
     *
     * @param userId       요청 유저 ID
     * @param friendshipId 친구 데이터 ID
     */
    @Transactional
    public void declineFriendshipRequest(long userId, long friendshipId) {
        Friendship foundFriendship = getFriendshipsById(friendshipId);
        foundFriendship.decline(userId);
    }

    /**
     * 보낸 친구 요청 취소하기
     *
     * @param userId
     * @param friendshipId
     */
    @Transactional
    public void cancelFriendshipRequest(long userId, long friendshipId) {
        Friendship foundFriendship = getFriendshipsById(friendshipId);
        foundFriendship.cancel(userId);
    }

    /**
     * 친구 별칭 수정하기
     *
     * @param userId
     * @param friendshipId
     */
    @Transactional
    public void updateFriendshipAlias(long userId, long friendshipId, UpdateFriendshipAliasAppRequest request) {
        Friendship foundFriendship = getFriendshipsById(friendshipId);
        if (!foundFriendship.isAccepted()) {
            throw new DomainException(FriendshipError.INVALID_REQUEST);
        }
        foundFriendship.updateFriendAlias(userId, request.newUserAlias());
    }

    private Friendship getFriendshipsById(long id) {
        return friendshipRepository
                .findById(id).orElseThrow(() -> new DomainException(FriendshipError.FRIENDSHIP_NOT_FOUND));
    }

    private List<Friendship> getFriendshipsByIds(List<Long> request) {
        return friendshipRepository.findByIdIn(request);
    }
}

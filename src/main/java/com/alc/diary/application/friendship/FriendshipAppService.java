package com.alc.diary.application.friendship;

import com.alc.diary.application.friendship.dto.request.RequestFriendshipAppRequest;
import com.alc.diary.application.friendship.dto.response.GetFriendshipsAppResponse;
import com.alc.diary.application.friendship.dto.response.GetPendingRequestsAppResponse;
import com.alc.diary.application.friendship.dto.response.GetReceivedFriendshipRequestsAppResponse;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.friendship.Friendship;
import com.alc.diary.domain.friendship.enums.FriendshipStatus;
import com.alc.diary.domain.friendship.error.FriendshipError;
import com.alc.diary.domain.friendship.repository.FriendshipRepository;
import com.alc.diary.domain.user.User;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FriendshipAppService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

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

        validRequest(requester, targetUser);

        Friendship friendshipToSave =
                Friendship.createRequest(requester, targetUser, request.message());
        friendshipRepository.save(friendshipToSave);
    }

    private User getUserById(long userId) {
        return userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND, "User ID: " + userId));
    }

    private void validRequest(User requester, User targetUser) {
        if (requester.equals(targetUser)) {
            throw new DomainException(
                    FriendshipError.INVALID_REQUEST,
                    String.format("Request UserID: %d, Target User Id: %d", requester.getId(), targetUser.getId())
            );
        }
        if (doesFriendshipExist(requester, targetUser)) {
            throw new DomainException(FriendshipError.ALREADY_SENT_REQUEST);
        }
    }

    private boolean doesFriendshipExist(User requester, User targetUser) {
        return friendshipRepository.findByUserIds(requester.getId(), targetUser.getId()).stream()
                .anyMatch(Friendship::isRequestedOrAccepted);
    }

    /**
     * 현재 사용자의 친구 목록 조회
     *
     * @param userId 요청 유저 ID
     * @return
     */
    public GetFriendshipsAppResponse getFriendships(long userId) {
        return GetFriendshipsAppResponse.of(
                filterFriendshipWithActiveUsers(friendshipRepository.findAcceptedFriendshipsByUserId(userId)), userId
        );
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
     * 수락 대기중인 친구 요청 목록 조회
     *
     * @param userId
     * @return
     */
    public List<GetPendingRequestsAppResponse> getPendingRequests(long userId) {
        return GetPendingRequestsAppResponse.from(
                filterFriendshipWithActiveUsers(friendshipRepository.findRequestedFriendshipByFromUserId(userId))
        );
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
    public void acceptFriendshipRequest(long userId, long friendshipId) {
        Friendship foundFriendShip = getFriendshipsById(friendshipId);
        foundFriendShip.accept(userId);
    }

    /**
     * 친구 삭제 (soft delete)
     *
     * @param requesterId  요청 유저 ID
     * @param friendshipId 삭제할 친구 데이터 ID
     */
    @Transactional
    public void deleteFriendship(long requesterId, long friendshipId) {
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

    private Friendship getFriendshipsById(long id) {
        return friendshipRepository
                .findById(id).orElseThrow(() -> new DomainException(FriendshipError.FRIENDSHIP_NOT_FOUND));
    }

    private List<Friendship> getFriendshipsByIds(List<Long> request) {
        return friendshipRepository.findByIdIn(request);
    }
}

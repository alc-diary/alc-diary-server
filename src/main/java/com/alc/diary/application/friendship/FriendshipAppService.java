package com.alc.diary.application.friendship;

import com.alc.diary.application.friendship.dto.request.AcceptFriendshipRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.DeclineFriendshipRequestAppRequest;
import com.alc.diary.application.friendship.dto.request.RequestFriendshipAppRequest;
import com.alc.diary.application.friendship.dto.response.GetFriendshipsAppResponse;
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
import java.util.Optional;
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
     * @param userId 요청 유저 ID
     * @param request request
     */
    @Transactional
    public void requestFriendship(long userId, RequestFriendshipAppRequest request) {
        User requester = getUserById(userId);
        User targetUser = getUserByNickname(request.targetNickname());
        if (requester.equals(targetUser)) {
            throw new DomainException(FriendshipError.INVALID_REQUEST);
        }
        if (friendshipRepository.findByFromUser_IdAndToUser_Id(requester.getId(), targetUser.getId()).stream()
                                .anyMatch(friendship -> friendship.getStatus() == FriendshipStatus.REQUESTED || friendship.getStatus() == FriendshipStatus.ACCEPTED)) {
            throw new DomainException(FriendshipError.ALREADY_SENT_REQUEST);
        }
        Friendship friendshipToSave =
                Friendship.createRequest(requester, targetUser, request.alias(), request.message());
        friendshipRepository.save(friendshipToSave);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
    }

    private User getUserByNickname(String nickname) {
        return userRepository.findByDetail_Nickname(nickname).orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
    }

    /**
     * 친구 목록 조회
     *
     * @param userId 요청 유저 ID
     * @return
     */
    public GetFriendshipsAppResponse getFriendships(long userId) {
        return GetFriendshipsAppResponse.of(friendshipRepository.findAcceptedFriendshipsByUserId(userId), userId);
    }

    /**
     * 받은 친구 요청 리스트 조회
     *
     * @param userId 요청 유저 ID
     * @return 받은 친구 요청 리스트
     */
    public List<GetReceivedFriendshipRequestsAppResponse> getReceivedFriendshipRequests(long userId) {
        List<Friendship> foundFriendships = getFriendshipsByUserIdAndStatus(userId, FriendshipStatus.REQUESTED);
        return foundFriendships.stream()
                .map(GetReceivedFriendshipRequestsAppResponse::from)
                .collect(Collectors.toList());
    }

    private List<Friendship> getFriendshipsByUserIdAndStatus(long userId, FriendshipStatus status) {
        return friendshipRepository.findByToUser_IdAndStatusEquals(userId, status);
    }

    /**
     * 친구 요청 수락
     *
     * @param userId 요청 유저 ID
     * @param request request
     */
    @Transactional
    public void acceptFriendshipRequest(long userId, long friendshipId, AcceptFriendshipRequestAppRequest request) {
        Friendship foundFriendShip = getFriendshipsById(friendshipId)
                .orElseThrow(() -> new DomainException(FriendshipError.INVALID_REQUEST));
        foundFriendShip.accept(userId, request.alias());
    }

    /**
     * 친구 요청 거절
     *
     * @param userId 요청 유저 ID
     * @param request request
     */
    @Transactional
    public void declineFriendshipRequest(long userId, DeclineFriendshipRequestAppRequest request) {
        getFriendshipsByIds(request.requestIds()).forEach(it -> it.decline(userId));
    }

    private Optional<Friendship> getFriendshipsById(long id) {
        return friendshipRepository.findById(id);
    }

    private List<Friendship> getFriendshipsByIds(List<Long> request) {
        return friendshipRepository.findByIdIn(request);
    }

    /**
     * 친구 삭제
     *
     * @param requesterId 요청 유저 ID
     * @param friendshipId 삭제할 친구 데이터 ID
     */
    @Transactional
    public void deleteFriendship(long requesterId, long friendshipId) {
        friendshipRepository.findByIdAndStatusEquals(friendshipId, FriendshipStatus.ACCEPTED)
                .filter(friendship -> friendship.isUserInvolvedInFriendship(requesterId))
                .ifPresent(friendshipRepository::delete);
    }
}

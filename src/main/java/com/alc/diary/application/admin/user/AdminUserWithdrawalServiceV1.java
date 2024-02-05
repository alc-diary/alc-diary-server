package com.alc.diary.application.admin.user;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.error.UserWithdrawalError;
import com.alc.diary.domain.user.repository.UserWithdrawalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminUserWithdrawalServiceV1 {

    private final UserWithdrawalRepository userWithdrawalRepository;

    public Page<UserWithdrawalDto> getAllUserWithdrawals(Pageable pageable) {
        return userWithdrawalRepository.findAll(pageable)
                .map(UserWithdrawalDto::fromDomainModel);
    }

    public UserWithdrawalDto getUserWithdrawalById(long userWithdrawalId) {
        return userWithdrawalRepository.findById(userWithdrawalId)
                .map(UserWithdrawalDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(UserWithdrawalError.USER_WITHDRAWAL_NOT_FOUND));
    }
}

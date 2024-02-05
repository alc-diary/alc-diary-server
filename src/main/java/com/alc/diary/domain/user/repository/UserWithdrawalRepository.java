package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserWithdrawal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserWithdrawalRepository extends Repository<UserWithdrawal, Long> {

    UserWithdrawal save(UserWithdrawal userWithdrawal);

    Page<UserWithdrawal> findAll(Pageable pageable);

    Optional<UserWithdrawal> findById(long userWithdrawalId);
}

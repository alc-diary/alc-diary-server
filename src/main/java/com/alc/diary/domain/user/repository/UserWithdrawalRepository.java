package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserWithdrawal;
import org.springframework.data.repository.Repository;

public interface UserWithdrawalRepository extends Repository<UserWithdrawal, Long> {

    UserWithdrawal save(UserWithdrawal userWithdrawal);
}

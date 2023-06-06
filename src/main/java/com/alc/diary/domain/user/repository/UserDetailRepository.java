package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.UserDetail;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserDetailRepository extends Repository<UserDetail, Long> {

    Optional<UserDetail> findByNickname(String nickname);

    Boolean existsByNickname(String nickname);
}

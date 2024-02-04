package com.alc.diary.application.admin.user;

import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.error.UserError;
import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDto::fromDomainModel);
    }

    public UserDto getUserById(long userId) {
        return userRepository.findById(userId)
                .map(UserDto::fromDomainModel)
                .orElseThrow(() -> new DomainException(UserError.USER_NOT_FOUND));
    }
}

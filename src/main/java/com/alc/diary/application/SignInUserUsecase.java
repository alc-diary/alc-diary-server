package com.alc.diary.application;

import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInUserUsecase {

    private final UserRepository userRepository;

    public void execute() {
    }
}

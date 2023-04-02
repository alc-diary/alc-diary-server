package com.alc.diary.application.user;

import com.alc.diary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SignInUseCase {

    private final UserRepository userRepository;

    public void execute() {
    }
}

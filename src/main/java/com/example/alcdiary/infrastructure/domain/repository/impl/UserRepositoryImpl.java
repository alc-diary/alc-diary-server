package com.example.alcdiary.infrastructure.domain.repository.impl;

import com.example.alcdiary.domain.model.UserModel;
import com.example.alcdiary.domain.repository.UserRepository;
import com.example.alcdiary.infrastructure.entity.user.User;
import com.example.alcdiary.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserModel save(UserModel userModel) {
        System.out.println(userModel.getSocialType());
        User user = User.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .nickname(userModel.getNickname())
                .profileImageUrl(userModel.getProfileImageUrl())
                .gender(userModel.getGender())
                .socialType(userModel.getSocialType())
                .build();
        User savedUser = userJpaRepository.save(user);
        return UserModel.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .gender(savedUser.getGender())
                .socialType(savedUser.getSocialType())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }

    @Override
    public Optional<UserModel> findByIdAndSocialType(Long id, UserModel.SocialType socialType) {
        return userJpaRepository.findByIdAndSocialType(id, socialType)
                .map(user -> UserModel.builder()
                            .id(user.getId())
                            .nickname(user.getNickname())
                            .gender(user.getGender())
                            .profileImageUrl(user.getProfileImageUrl())
                            .build());
    }
}

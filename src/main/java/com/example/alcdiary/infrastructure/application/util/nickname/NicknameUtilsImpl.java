package com.example.alcdiary.infrastructure.application.util.nickname;

import com.example.alcdiary.application.util.nickname.NicknameUtils;
import com.example.alcdiary.infrastructure.entity.Nickname;
import com.example.alcdiary.infrastructure.jpa.NicknameJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class NicknameUtilsImpl implements NicknameUtils {

    private final NicknameJpaRepository nicknameJpaRepository;

    @Override
    public String generateNickname() {
        Nickname first = nicknameJpaRepository
                .findByLocation(Nickname.ELocation.FIRST.toString())
                .get(0);
        Nickname second = nicknameJpaRepository
                .findByLocation(Nickname.ELocation.SECOND.toString())
                .get(0);

        return first.getKeyword() + " " + second.getKeyword();
    }
}

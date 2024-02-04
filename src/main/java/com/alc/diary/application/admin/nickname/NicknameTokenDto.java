package com.alc.diary.application.admin.nickname;

import com.alc.diary.domain.user.NicknameToken;

public record NicknameTokenDto(

        long id,
        String token
) {

    public static NicknameTokenDto fromDomainModel(NicknameToken nicknameToken) {
        return new NicknameTokenDto(
                nicknameToken.getId(),
                nicknameToken.getToken()
        );
    }
}

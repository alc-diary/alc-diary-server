package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomNicknameTokenRepository {

    Page<NicknameToken> findByOrdinal(Pageable pageable, NicknameTokenOrdinal ordinal);
}

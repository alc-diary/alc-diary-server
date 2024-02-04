package com.alc.diary.application.admin.nickname;

import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import com.alc.diary.domain.user.repository.NicknameTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminNicknameService {

    private final NicknameTokenRepository nicknameTokenRepository;

    public Page<NicknameTokenDto> getAllFirstNicknameToken(Pageable pageable) {
        return nicknameTokenRepository.findByOrdinal(pageable, NicknameTokenOrdinal.FIRST)
                .map(NicknameTokenDto::fromDomainModel);
    }

    public Page<NicknameTokenDto> getAllSecondNicknameToken(Pageable pageable) {
        return nicknameTokenRepository.findByOrdinal(pageable, NicknameTokenOrdinal.SECOND)
                .map(NicknameTokenDto::fromDomainModel);
    }
}

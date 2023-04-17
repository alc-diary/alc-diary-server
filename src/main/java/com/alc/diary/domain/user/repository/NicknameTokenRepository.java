package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface NicknameTokenRepository extends Repository<NicknameToken, Long> {

    NicknameToken save(NicknameToken nicknameToken);

    @Query(
        value = "SELECT * " +
                "FROM nickname_tokens nt " +
                "WHERE nt.ordinal = :ordinal " +
                "ORDER BY RAND() " +
                "LIMIT 1",
        nativeQuery = true
    )
    Optional<NicknameToken> findByOrdinalOrderByRandLimit1(NicknameTokenOrdinal ordinal);

    List<NicknameToken> findByOrdinal(NicknameTokenOrdinal ordinal);

    void deleteById(Long id);
}

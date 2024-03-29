package com.alc.diary.domain.user.repository;

import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface NicknameTokenRepository extends Repository<NicknameToken, Long>, CustomNicknameTokenRepository {

    NicknameToken save(NicknameToken nicknameToken);

    @Query("SELECT nt " +
           "FROM NicknameToken nt " +
           "WHERE nt.ordinal = :ordinal " +
           "ORDER BY FUNCTION('RAND') ")
    List<NicknameToken> findByOrdinalOrderByRandLimit1(NicknameTokenOrdinal ordinal, Pageable pageable);

    List<NicknameToken> findByOrdinal(NicknameTokenOrdinal ordinal);

    void deleteById(Long id);
}

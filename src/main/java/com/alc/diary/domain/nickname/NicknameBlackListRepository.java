package com.alc.diary.domain.nickname;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface NicknameBlackListRepository extends Repository<BannedWord, Long> {

    BannedWord save(BannedWord bannedWord);

    List<BannedWord> findAll();

    void deleteById(long id);
}

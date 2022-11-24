package com.example.alcdiary.infrastructure.jpa;

import com.example.alcdiary.infrastructure.entity.Nickname;
import com.example.alcdiary.infrastructure.entity.NicknamePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NicknameJpaRepository extends JpaRepository<Nickname, NicknamePK> {

    @Query(value = "" +
            "SELECT " +
            "   n.keyword keyword, " +
            "   n.location location " +
            "FROM " +
            "   alc.nickname n " +
            "WHERE " +
            "   n.location = :location " +
            "ORDER BY " +
            "   RAND() " +
            "LIMIT 1 ",
    nativeQuery = true)
    List<Nickname> findByLocation(String location);
}

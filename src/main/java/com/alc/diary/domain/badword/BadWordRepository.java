package com.alc.diary.domain.badword;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface BadWordRepository extends Repository<BadWord, Long> {

    List<BadWord> findAll();
}

package com.alc.diary.domain.calendar.repository;

import com.alc.diary.domain.calendar.Photo;
import org.springframework.data.repository.Repository;

import java.util.Collection;

public interface PhotoRepository extends Repository<Photo, Long> {

    void deleteByIdIn(Collection<Long> id);
}

package com.alc.diary.domain;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "revinfo")
@RevisionEntity
public class CustomRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private long id;

    @RevisionTimestamp
    private long timestamp;

    private LocalDateTime revisionDate;

    @PrePersist
    public void prePersist() {
        revisionDate = LocalDateTime.now();
    }
}

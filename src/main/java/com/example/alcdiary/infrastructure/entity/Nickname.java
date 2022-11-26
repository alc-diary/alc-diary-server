package com.example.alcdiary.infrastructure.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@IdClass(NicknamePK.class)
@Table(name = "nickname")
@Entity
public class Nickname {

    @Column(name = "keyword", length = 50, nullable = false)
    @Id
    private String keyword;

    @Column(name = "location", nullable = false)
    @Enumerated(EnumType.STRING)
    @Id
    private ELocation location;

    public enum ELocation {

        FIRST,
        SECOND;

        public static ELocation from(String s) {
            return ELocation.valueOf(s);
        }
    }

    protected Nickname() {
    }

    public static Nickname of(String keyword, ELocation location) {
        Nickname nickname = new Nickname();
        nickname.keyword = keyword;
        nickname.location = location;
        return nickname;
    }
}

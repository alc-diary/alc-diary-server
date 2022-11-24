package com.example.alcdiary.infrastructure.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class NicknamePK implements Serializable {

    private String keyword;
    private Nickname.ELocation location;
}

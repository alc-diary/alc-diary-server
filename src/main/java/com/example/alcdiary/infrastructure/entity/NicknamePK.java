package com.example.alcdiary.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NicknamePK implements Serializable {

    private String keyword;
    private Nickname.ELocation location;
}

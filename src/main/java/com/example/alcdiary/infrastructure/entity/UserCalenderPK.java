package com.example.alcdiary.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserCalenderPK implements Serializable {
    private String userId;
    private Long calenderId;
}

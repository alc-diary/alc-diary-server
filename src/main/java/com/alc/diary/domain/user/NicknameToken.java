package com.alc.diary.domain.user;

import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "nickname_tokens",
uniqueConstraints = {@UniqueConstraint(columnNames = {"token", "ordinal"})})
@Entity
public class NicknameToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ordinal", length = 10, nullable = false)
    private NicknameTokenOrdinal ordinal;

    @Column(name = "token", length = 20, nullable = false)
    private String token;

    public NicknameToken(NicknameTokenOrdinal ordinal, String token) {
        this.ordinal = ordinal;
        this.token = token;
    }
}

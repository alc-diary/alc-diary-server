package com.alc.diary.domain.nickname;

import com.alc.diary.domain.exception.DomainException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "nickname_banned_words")
@Entity
public class BannedWord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", length = 16, nullable = false, unique = true)
    private String word;

    public BannedWord(String word) {
        if (StringUtils.isEmpty(word)) {
            throw new DomainException(NicknameBannedWordError.WORD_NULL);
        }
        if (StringUtils.length(word) > 16) {
            throw new DomainException(NicknameBannedWordError.NICKNAME_BANNED_WORD_LENGTH_EXCEEDED);
        }
        this.word = word;
    }
}

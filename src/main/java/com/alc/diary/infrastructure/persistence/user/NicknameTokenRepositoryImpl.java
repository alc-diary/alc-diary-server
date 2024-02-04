package com.alc.diary.infrastructure.persistence.user;

import com.alc.diary.domain.user.NicknameToken;
import com.alc.diary.domain.user.QNicknameToken;
import com.alc.diary.domain.user.enums.NicknameTokenOrdinal;
import com.alc.diary.domain.user.repository.CustomNicknameTokenRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.alc.diary.domain.user.QNicknameToken.nicknameToken;

@RequiredArgsConstructor
public class NicknameTokenRepositoryImpl implements CustomNicknameTokenRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<NicknameToken> findByOrdinal(Pageable pageable, NicknameTokenOrdinal ordinal) {
        Long total = jpaQueryFactory.select(nicknameToken.count())
                .from(nicknameToken)
                .fetchFirst();
        List<NicknameToken> content = jpaQueryFactory.selectFrom(nicknameToken)
                .where(nicknameToken.ordinal.eq(ordinal))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        return new PageImpl(content, pageable, total);
    }
}

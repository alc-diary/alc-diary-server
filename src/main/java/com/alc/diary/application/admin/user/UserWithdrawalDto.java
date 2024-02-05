package com.alc.diary.application.admin.user;

import com.alc.diary.domain.user.UserWithdrawal;

import java.time.LocalDateTime;

public record UserWithdrawalDto(

        long id,
        long userId,
        String deletionReason,
        LocalDateTime deletedAt
) {

    public static UserWithdrawalDto fromDomainModel(UserWithdrawal domainModel) {
        return new UserWithdrawalDto(
                domainModel.getId(),
                domainModel.getUser().getId(),
                domainModel.getDeletionReason(),
                domainModel.getDeletedAt()
        );
    }
}

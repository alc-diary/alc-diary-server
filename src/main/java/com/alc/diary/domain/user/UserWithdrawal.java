package com.alc.diary.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_withdrawals")
@Entity
public class UserWithdrawal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "deletion_reason", length = 1000, updatable = false)
    private String deletionReason;

    private LocalDateTime deletedAt;

    public UserWithdrawal(Long id, User user, String deletionReason, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.deletionReason = deletionReason;
        this.deletedAt = deletedAt;
    }

    public static UserWithdrawal of(User user, String deletionReason) {
        return new UserWithdrawal(null, user, deletionReason, LocalDateTime.now());
    }
}

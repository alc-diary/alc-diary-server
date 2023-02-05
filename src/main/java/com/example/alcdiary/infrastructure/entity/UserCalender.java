package com.example.alcdiary.infrastructure.entity;

import com.example.alcdiary.domain.enums.EditRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@IdClass(UserCalenderPK.class)
@Table(name = "user_calender")
public class UserCalender {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "calender_id")
    private Long calenderId;

    @Column(name = "edit_role")
    @Enumerated(EnumType.STRING)
    private EditRole editRole;


    public UserCalender(
            String userId,
            Long calenderId,
            EditRole editRole
    ) {
        this.userId = userId;
        this.calenderId = calenderId;
        this.editRole = editRole;
    }

}

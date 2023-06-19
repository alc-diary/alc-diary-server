package com.alc.diary.domain.usercalendar;

import com.alc.diary.domain.BaseCreationEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Getter
@ToString(exclude = "userCalendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_calendar_images")
@Entity
public class UserCalendarImage extends BaseCreationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_calendar_id", foreignKey = @ForeignKey(name = "fk_user_calendar_images_user_calendars"))
    private UserCalendar userCalendar;

    @Column(name = "image_url", length = 1000, nullable = false)
    private String imageUrl;

    public UserCalendarImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserCalendar(UserCalendar userCalendar) {
        this.userCalendar = userCalendar;
    }
}

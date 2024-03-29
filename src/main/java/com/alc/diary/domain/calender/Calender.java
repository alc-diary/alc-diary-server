package com.alc.diary.domain.calender;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.calender.error.CalenderError;
import com.alc.diary.domain.calender.model.CalenderImage;
import com.alc.diary.domain.calender.model.DrinkModel;
import com.alc.diary.domain.calender.model.DrinkModelConverter;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.TypeDef;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "calender")
public class Calender extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 20)
    private String title;

    @Column(name = "contents", length = 1024)
    private String contents;

    @Column(name = "drink_start_date_time")
    private LocalDateTime drinkStartDateTime;

    @Column(name = "drink_end_date_time")
    private LocalDateTime drinkEndDateTime;

    @Column(name = "drink_models", length = 500)
    @Convert(converter = DrinkModelConverter.class)
    private List<DrinkModel> drinkModels;

    @Embedded
    public CalenderImage image;

    @Column(name = "drink_condition", length = 10)
    private String drinkCondition;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    public User user;

    public Calender(String title, String contents, LocalDateTime drinkStartDateTime, LocalDateTime drinkEndDateTime, List<DrinkModel> drinkModels, CalenderImage image, String drinkCondition, User user) {
        if (!StringUtils.hasText(title) || drinkStartDateTime == null || drinkEndDateTime == null || drinkModels == null || user == null)
            throw new DomainException(CalenderError.INVALID_PARAMETER_INCLUDE);
        this.title = title;
        this.contents = contents;
        this.drinkStartDateTime = drinkStartDateTime;
        this.drinkEndDateTime = drinkEndDateTime;
        this.drinkModels = drinkModels;
        this.image = image;
        this.drinkCondition = drinkCondition;
        this.user = user;
    }

    public static Calender Of(LocalDateTime drinkStartDateTime, List<DrinkModel> drinkModels, User user) {
        val defaultTitle = "오늘의 음주기록";
        return Calender.builder()
                .title(defaultTitle)
                .drinkStartDateTime(drinkStartDateTime)
                .drinkEndDateTime(LocalDateTime.now())
                .drinkModels(drinkModels)
                .user(user)
                .build();
    }

    public void update(String title, String contents, LocalDateTime drinkStartDateTime, LocalDateTime drinkEndDateTime, List<DrinkModel> drinkModels, CalenderImage image, String drinkCondition, User user) {
        if (!StringUtils.hasText(title) || drinkStartDateTime == null || drinkEndDateTime == null || drinkModels == null || user == null)
            throw new DomainException(CalenderError.INVALID_PARAMETER_INCLUDE);
        this.title = title;
        this.contents = contents;
        this.drinkStartDateTime = drinkStartDateTime;
        this.drinkEndDateTime = drinkEndDateTime;
        this.drinkModels = drinkModels;
        this.image = image;
        this.drinkCondition = drinkCondition;
        this.user = user;
    }

    public List<DrinkModel> getDrinkModels() {
        return drinkModels;
    }
}

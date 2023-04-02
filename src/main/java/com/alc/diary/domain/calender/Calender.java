package com.alc.diary.domain.calender;

import com.alc.diary.domain.BaseEntity;
import com.alc.diary.domain.exception.DomainException;
import com.alc.diary.domain.user.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "calender")
public class Calender extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    @Lob
    private String contents;

    @Column(name = "drink_start_date_time")
    private LocalDateTime drinkStartDateTime;

    @Column(name = "drink_end_date_time")
    private LocalDateTime drinkEndDateTime;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    // TODO: 컨버터
    private List<DrinkModel> drinkModel;

    @Embedded
    public CalenderImage image;

    @Column(name = "condition")
    private String condition;

    @JoinColumn(name = "user_id", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Calender(String title, String contents, LocalDateTime drinkStartDateTime, LocalDateTime drinkEndDateTime, List<DrinkModel> drinkModel, CalenderImage image, String condition, User user) {
        if (!StringUtils.hasText(title) || drinkStartDateTime == null || drinkEndDateTime == null || drinkModel.isEmpty() || user == null)
            throw new DomainException();
        this.title = title;
        this.contents = contents;
        this.drinkStartDateTime = drinkStartDateTime;
        this.drinkEndDateTime = drinkEndDateTime;
        this.drinkModel = drinkModel;
        this.image = image;
        this.condition = condition;
        this.user = user;
    }
}

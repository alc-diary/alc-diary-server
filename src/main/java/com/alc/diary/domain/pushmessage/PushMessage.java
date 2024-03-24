package com.alc.diary.domain.pushmessage;

import com.alc.diary.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "push_messages")
@Entity
public class PushMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "body", nullable = false)
    private String body;

    @NotNull
    @Column(name = "event_name", length = 30, nullable = false)
    private String eventName;

    private PushMessage(String title, String body, String eventName) {
        this.title = title;
        this.body = body;
        this.eventName = eventName;
    }

    public static PushMessage create(String title, String body, String eventName) {
        return new PushMessage(title, body, eventName);
    }
}

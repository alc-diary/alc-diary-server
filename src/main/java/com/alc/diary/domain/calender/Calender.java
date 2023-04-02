package com.alc.diary.domain.calender;

import javax.persistence.*;

@Entity
@Table(name = "calender")
public class Calender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

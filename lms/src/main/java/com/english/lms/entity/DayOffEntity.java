package com.english.lms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "lms_day_off")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DayOffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dayOffNum;

    @Column(name = "off_date")
    private java.sql.Date offDate;

    @Column(name = "name")
    private String name;
    
}
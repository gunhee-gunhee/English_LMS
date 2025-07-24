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
@Table(name = "lms_class")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_num")
    private Integer classNum;

    @Column(name = "student_num")
    private Integer studentNum;

    @Column(name = "teacher_num")
    private Integer teacherNum;

    @Column(name = "week_days")
    private String weekDays; 

    @Column(name = "text_num")
    private Integer textNum;
    
    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    // 추가된 부분: 월 정보(YYYY-MM)
    @Column(name = "class_month", length = 10)
    private String classMonth;
}

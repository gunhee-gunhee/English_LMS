package com.english.lms.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

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

@Entity
@Table(name = "lms_teacher_request")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequestEntity {

    // 신청번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_num")
    private Integer requestNum;

    // 학생번호
    @Column(name = "student_num", length = 100)
    private String studentNum;

    // 요일
    @Column(name = "week_days", length = 10)
    private String weekDays;

    // 시작 시간
    @Column(name = "start_time")
    private LocalTime startTime;

    // 종료 시간
    @Column(name = "end_time")
    private LocalTime endTime;

    // 신청월
    @Column(name = "start_month")
    private Integer startMonth;

    // 책임여부
    @Column(name = "responsibility")
    private Boolean responsibility;

    // 강사 A 이름
    @Column(name = "teacher_a_name", length = 40)
    private String teacherAName;

    // 강사 B 이름
    @Column(name = "teacher_b_name", length = 40)
    private String teacherBName;

    // 강사 C 이름
    @Column(name = "teacher_c_name", length = 40)
    private String teacherCName;

    // 강사 A 주
    @Column(name = "teacher_a_week", length = 10)
    private String teacherAWeek;

    // 강사 B 주
    @Column(name = "teacher_b_week", length = 10)
    private String teacherBWeek;

    // 강사 C 주
    @Column(name = "teacher_c_week", length = 10)
    private String teacherCWeek;

    // 강사 A 시간
    @Column(name = "teacher_a_time")
    private LocalTime teacherATime;

    // 강사 B 시간
    @Column(name = "teacher_b_time")
    private LocalTime teacherBTime;

    // 강사 C 시간
    @Column(name = "teacher_c_time")
    private LocalTime teacherCTime;

    // 신청일
    @Column(name = "request_date")
    private LocalDateTime requestDate;
}

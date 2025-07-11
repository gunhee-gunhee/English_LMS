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
@Table(name = "lms_day_class")
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
@Builder
public class DayClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_class_num")
    private Integer dayClassNum;
    
    @Column(name = "student_num")
    private Integer studentNum;
    
    @Column(name = "teacher_num")
    private Integer teacherNum;

    @Column(name = "class_num")
    private Integer classNum;

    @Column(name = "class_type")
    private String classType;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_date")
    private LocalDate classDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "zoom_link")
    private String zoomLink;

    @Column(name = "zoom_meeting_id")
    private String zoomMeetingId;

    @Column(name = "attendance")
    private Boolean attendance;

    @Column(name = "absent")
    private String absent;

    @Column(name = "comment")
    private String comment;

    @Column(name = "progress")
    private String progress;
}

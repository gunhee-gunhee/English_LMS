package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDTO {

    private Integer classNum;
    private Integer studentNum;
    private Integer teacherNum;
    private String weekDays;
    private Integer textNum;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String studentId;
    private String studentNickname;
    private String studentNicknameJp;
    private Integer studentEnglishLevel;
    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;
    private String teacherId;
    private String teacherNickname;
    private String textName;
    private String classMonth;
    private String classType;
    
    
    
    
}

package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherMyPageDTO {
    private int dayClassNum;
    private String startTime;
    private String endTime;
    private String studentName;
    private String studentNameJp;
    private String classType;
    private String className;
    private String absent;
    private boolean attendance;
}

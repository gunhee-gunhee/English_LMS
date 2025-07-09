package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {
    private String id;
    private String password;
    private String passwordCheck; // 폼에서 입력 받음
    private String nickname;
    private String zoomId;
    private LocalDateTime join_date;
    private List<String> weekdays;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private LocalDate endDate;
    private int startHour;
    private int endHour;
    private int startMinute;
    private int endMinute;
    private Integer nullity;
    
 
}

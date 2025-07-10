package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private String startDate;
    private String endDate;
    private String startHour;
    private String endHour;
    private String startMinute;
    private String endMinute;
    private Boolean nullity;
    
 
}

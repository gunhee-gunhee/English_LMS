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
public class TeacherDTO {
	private Integer teacherNum;
    private String id;
    private String password;
    private String passwordCheck; 
    private String nickname;
    private String zoomId;
    private LocalDateTime join_date;
    private Boolean nullity;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private LocalDate endDate;
    private String role;
    private List<TeacherScheduleDTO> schedules= new ArrayList<>();
 
}

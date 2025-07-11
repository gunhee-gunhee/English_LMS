package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherScheduleDTO {

    
    private List<String> weekdays;
    
    private Integer startHour;
    private Integer startMinute;
    
    private Integer endHour;
    private Integer endMinute;
    
    @Override
    public String toString() {
        return String.format("[요일: %s, %02d:%02d ~ %02d:%02d]", weekdays, startHour, startMinute, endHour, endMinute);
    }
    
}

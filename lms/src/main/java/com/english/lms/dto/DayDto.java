package com.english.lms.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DayDto {
    private LocalDate date;
    private String dayLabel;     
    private boolean isHoliday;
    private String holidayName;
    private List<DayClassDto> lessons;
    private String lessonTimesStr;
    private String classTypesStr;
    private boolean isWeekend;    
}

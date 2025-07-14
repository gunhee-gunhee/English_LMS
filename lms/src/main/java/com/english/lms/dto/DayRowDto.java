package com.english.lms.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DayRowDto {
    private LocalDate date;           // 날짜(첫줄만)
    private boolean first;            // 첫줄인지
    private int rowspan;              // 병합할 개수
    private String dayLabel;
    private boolean holiday;
    private String holidayName;
    private String dateClass;
    private DayClassDTO lesson;       // null이면 수업 없음
}

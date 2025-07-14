package com.english.lms.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DayDTO {
    private LocalDate date;
    private String dayLabel;     
    private boolean isHoliday;         
    private String holidayName;        
    private List<DayClassDTO> lessons; 
    private String lessonTimesStr;     
    private String classTypesStr;
    private String teacherNamesStr;
    private boolean isWeekend;         
    private String dateClass = "";  

    // ★ DayRowDto 리스트 반환 메서드 추가
    public List<DayRowDto> getRowDtoList() {
        List<DayRowDto> list = new java.util.ArrayList<>();
        int rowCount = (lessons == null || lessons.isEmpty()) ? 1 : lessons.size();
        if (rowCount == 0) rowCount = 1;

        if (lessons == null || lessons.isEmpty()) {
            // 수업 없는 날
            DayRowDto row = new DayRowDto();
            row.setFirst(true);
            row.setRowspan(1);
            row.setDayLabel(dayLabel);
            row.setHoliday(isHoliday);
            row.setHolidayName(holidayName);
            row.setDateClass(dateClass);
            row.setLesson(null);
            list.add(row);
        } else {
            for (int i = 0; i < lessons.size(); i++) {
                DayRowDto row = new DayRowDto();
                row.setFirst(i == 0);
                row.setRowspan(i == 0 ? lessons.size() : 0);
                row.setDayLabel(dayLabel);
                row.setHoliday(isHoliday);
                row.setHolidayName(holidayName);
                row.setDateClass(dateClass);
                row.setLesson(lessons.get(i));
                list.add(row);
            }
        }
        return list;
    }
}


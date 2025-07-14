package com.english.lms.dto;

import com.english.lms.entity.DayClassEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DayClassDto {
    private Integer dayClassNum;
    private Integer studentNum;
    private Integer teacherNum;
    private Integer classNum;
    private String classType;
    private String className;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String zoomLink;
    private String zoomMeetingId;
    private Boolean attendance;
    private String absent;
    private String comment;
    private String progress;

    // ★★ Entity → DTO 변환 메서드
    public static DayClassDto from(DayClassEntity entity) {
        DayClassDto dto = new DayClassDto();
        dto.setDayClassNum(entity.getDayClassNum());
        dto.setStudentNum(entity.getStudentNum());
        dto.setTeacherNum(entity.getTeacherNum());
        dto.setClassNum(entity.getClassNum());
        dto.setClassType(entity.getClassType());
        dto.setClassName(entity.getClassName());
        dto.setClassDate(entity.getClassDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setZoomLink(entity.getZoomLink());
        dto.setZoomMeetingId(entity.getZoomMeetingId());
        dto.setAttendance(entity.getAttendance());
        dto.setAbsent(entity.getAbsent());
        dto.setComment(entity.getComment());
        dto.setProgress(entity.getProgress());
        return dto;
    }
}

package com.english.lms.dto;

import com.english.lms.entity.DayClassEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DayClassDTO {
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
    private Integer attendance;
    private Integer absent;
    private String comment;
    private String progress;
    private String teacherNickname;

    public static DayClassDTO from(DayClassEntity entity, String teacherNickname) {
        DayClassDTO dto = new DayClassDTO();
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
        dto.setTeacherNickname(teacherNickname); // 추가
        return dto;
    }

    public static DayClassDTO from(DayClassEntity entity) {
        return from(entity, "");
    }
}

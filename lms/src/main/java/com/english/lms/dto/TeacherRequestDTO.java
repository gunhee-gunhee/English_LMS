package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TeacherRequestDTO {

    /* 기본 키/식별 */
    private Integer requestNum;

    /* 학생 */
    private String studentNum;
    private String studentNickname;

    /* 수업 요일 문자열(예: "월·수·금") */
    private String weekDays;

    /* ---- 수업 시간: LocalTime + 시/분(레거시 호환) ---- */
    private LocalTime startTime;   // 템플릿에서 사용: #temporals.format(req.startTime, 'HH:mm')
    private LocalTime endTime;     // 템플릿에서 사용: #temporals.format(req.endTime, 'HH:mm')

    private Integer startHour;     // 레포지토리에서 시/분만 세팅하더라도 아래 getter가 LocalTime을 만들어 줌
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;

    /* 기타 표시용 */
    private Integer startMonth;        // MONTH(request_date)
    private Boolean responsibility;    // 컬럼 없음: null 예상
    private String  status;            // 화면 보조
    private LocalDate classDate;       // 화면 보조(미사용이면 무시)

    /* 강사 관련(요청서 내 선호 강사 등) */
    private String teacherAName;
    private String teacherBName;
    private String teacherCName;

    private String teacherAWeek;   // 컬럼 없음: null 예상
    private String teacherBWeek;   // 컬럼 없음: null 예상
    private String teacherCWeek;   // 컬럼 없음: null 예상

    // TIME 칼럼을 문자열 "HH:mm:ss" 혹은 "HH:mm" 형태로 받아 표시
    private String teacherATime;
    private String teacherBTime;
    private String teacherCTime;

    /* 요청일시 */
    private LocalDateTime requestDate;

    /* ---------- 동기화 유틸 ---------- */
    private void recomputeStartTimeFromParts() {
        if (this.startHour != null && this.startMinute != null) {
            this.startTime = LocalTime.of(this.startHour, this.startMinute);
        }
    }
    private void recomputeEndTimeFromParts() {
        if (this.endHour != null && this.endMinute != null) {
            this.endTime = LocalTime.of(this.endHour, this.endMinute);
        }
    }

    /* ---------- Getter/Setter ---------- */

    public Integer getRequestNum() {
        return requestNum;
    }

    public void setRequestNum(Integer requestNum) {
        this.requestNum = requestNum;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getStudentNickname() {
        return studentNickname;
    }

    public void setStudentNickname(String studentNickname) {
        this.studentNickname = studentNickname;
    }

    public String getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(String weekDays) {
        this.weekDays = weekDays;
    }

    /* startTime/endTime 는 우선 LocalTime이 있으면 그걸 반환,
       없으면 시/분 값으로 on-the-fly 생성하여 반환 */
    public LocalTime getStartTime() {
        if (startTime != null) return startTime;
        if (startHour != null && startMinute != null) {
            return LocalTime.of(startHour, startMinute);
        }
        return null;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        if (startTime != null) {
            this.startHour = startTime.getHour();
            this.startMinute = startTime.getMinute();
        }
    }

    public LocalTime getEndTime() {
        if (endTime != null) return endTime;
        if (endHour != null && endMinute != null) {
            return LocalTime.of(endHour, endMinute);
        }
        return null;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        if (endTime != null) {
            this.endHour = endTime.getHour();
            this.endMinute = endTime.getMinute();
        }
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
        if (this.startMinute != null) {
            recomputeStartTimeFromParts();
        }
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
        if (this.startHour != null) {
            recomputeStartTimeFromParts();
        }
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
        if (this.endMinute != null) {
            recomputeEndTimeFromParts();
        }
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
        if (this.endHour != null) {
            recomputeEndTimeFromParts();
        }
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    public Boolean getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(Boolean responsibility) {
        this.responsibility = responsibility;
    }

    public String getTeacherAName() {
        return teacherAName;
    }

    public void setTeacherAName(String teacherAName) {
        this.teacherAName = teacherAName;
    }

    public String getTeacherBName() {
        return teacherBName;
    }

    public void setTeacherBName(String teacherBName) {
        this.teacherBName = teacherBName;
    }

    public String getTeacherCName() {
        return teacherCName;
    }

    public void setTeacherCName(String teacherCName) {
        this.teacherCName = teacherCName;
    }

    public String getTeacherAWeek() {
        return teacherAWeek;
    }

    public void setTeacherAWeek(String teacherAWeek) {
        this.teacherAWeek = teacherAWeek;
    }

    public String getTeacherBWeek() {
        return teacherBWeek;
    }

    public void setTeacherBWeek(String teacherBWeek) {
        this.teacherBWeek = teacherBWeek;
    }

    public String getTeacherCWeek() {
        return teacherCWeek;
    }

    public void setTeacherCWeek(String teacherCWeek) {
        this.teacherCWeek = teacherCWeek;
    }

    public String getTeacherATime() {
        return teacherATime;
    }

    public void setTeacherATime(String teacherATime) {
        this.teacherATime = teacherATime;
    }

    public String getTeacherBTime() {
        return teacherBTime;
    }

    public void setTeacherBTime(String teacherBTime) {
        this.teacherBTime = teacherBTime;
    }

    public String getTeacherCTime() {
        return teacherCTime;
    }

    public void setTeacherCTime(String teacherCTime) {
        this.teacherCTime = teacherCTime;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
    }
}

package com.english.lms.service;

import com.english.lms.dto.IrregularAvailableTimeDTO;
import com.english.lms.dto.IrregularApplyRequestDTO;
import com.english.lms.dto.IrregularApplyResultDTO;
import com.english.lms.entity.DayClassEntity;
import com.english.lms.entity.TeacherScheduleEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.ZoomAccountEntity;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.TeacherScheduleRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.ZoomAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentIrregularApplyService {

    private final TeacherScheduleRepository teacherScheduleRepository;
    private final DayClassRepository dayClassRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ZoomAccountRepository zoomAccountRepository;

    // ==================== 수정된 부분: 20분 slot이 겹치는 예약 있으면 제외 ====================
    public List<IrregularAvailableTimeDTO> getAvailableTimes(LocalDate classDate) {
       
    	String weekDay = getWeekDayStringJapanese(classDate);

        List<TeacherScheduleEntity> slots = teacherScheduleRepository.findByWeekDayAndIsAvailable(weekDay);
        List<DayClassEntity> reserved = dayClassRepository.findByClassDate(classDate);

        // 예약된 20분 블록 목록 (start, end)
        List<LocalTime[]> reservedBlocks = reserved.stream()
            .map(d -> new LocalTime[] { d.getStartTime(), d.getEndTime() })
            .collect(Collectors.toList());

        Map<Integer, List<TeacherScheduleEntity>> byTeacher = slots.stream()
            .collect(Collectors.groupingBy(TeacherScheduleEntity::getTeacherNum));

        Map<String, List<Integer>> timeToTeachers = new LinkedHashMap<>();
        for (var entry : byTeacher.entrySet()) {
            Integer teacherNum = entry.getKey();
            List<TeacherScheduleEntity> schedule = entry.getValue().stream()
                .sorted(Comparator.comparing(TeacherScheduleEntity::getTimeSlot))
                .collect(Collectors.toList());

            for (int i = 0; i < schedule.size() - 1; i++) {
                LocalTime t1 = schedule.get(i).getTimeSlot();
                LocalTime t2 = schedule.get(i + 1).getTimeSlot();
                if (t2.equals(t1.plusMinutes(10))) {
                    LocalTime blockStart = t1;
                    LocalTime blockEnd = t1.plusMinutes(20);

                    // 예약된 수업의 (start, end)와 겹치는지 확인
                    boolean conflict = reservedBlocks.stream().anyMatch(res ->
                        !(blockEnd.compareTo(res[0]) <= 0 || blockStart.compareTo(res[1]) >= 0)
                    );
                    if (conflict) continue;

                    String start = t1.toString().substring(0,5);
                    String end = blockEnd.toString().substring(0,5);
                    String timeLabel = start + "~" + end;
                    timeToTeachers.computeIfAbsent(timeLabel, k -> new ArrayList<>()).add(teacherNum);
                }
            }
        }

        List<IrregularAvailableTimeDTO> result = new ArrayList<>();
        for (String timeLabel : timeToTeachers.keySet()) {
            result.add(IrregularAvailableTimeDTO.builder()
                    .timeLabel(timeLabel)
                    .teacherNums(timeToTeachers.get(timeLabel))
                    .build());
        }
        return result;
    }
    // ==================== 수정 끝 ====================

    @Transactional
    public IrregularApplyResultDTO applyIrregularClass(IrregularApplyRequestDTO request) {
        LocalDate classDate = LocalDate.parse(request.getDate());
        String weekDay = getWeekDayStringJapanese(classDate);
        String[] times = request.getTimeLabel().split("~");
        String start = times[0].trim();
        String end = times[1].trim();

        List<TeacherScheduleEntity> slots = teacherScheduleRepository.findByWeekDayAndIsAvailable(weekDay);
        List<DayClassEntity> reserved = dayClassRepository.findByClassDate(classDate);
        Set<String> reservedSet = reserved.stream()
            .map(d -> d.getTeacherNum() + "_" + d.getStartTime().toString())
            .collect(Collectors.toSet());

        Integer assignedTeacher = null;
        for (var entry : slots.stream().collect(Collectors.groupingBy(TeacherScheduleEntity::getTeacherNum)).entrySet()) {
            Integer teacherNum = entry.getKey();
            List<TeacherScheduleEntity> schedule = entry.getValue().stream()
                    .sorted(Comparator.comparing(TeacherScheduleEntity::getTimeSlot))
                    .collect(Collectors.toList());
            for (int i = 0; i < schedule.size() - 1; i++) {
                LocalTime t1 = schedule.get(i).getTimeSlot();
                LocalTime t2 = schedule.get(i + 1).getTimeSlot();
                if (t1.equals(LocalTime.parse(start + ":00")) && t2.equals(t1.plusMinutes(10))) {
                    if (reservedSet.contains(teacherNum + "_" + t1.toString())
                            || reservedSet.contains(teacherNum + "_" + t2.toString())) {
                        continue;
                    }
                    assignedTeacher = teacherNum;
                    break;
                }
            }
            if (assignedTeacher != null) break;
        }

        if (assignedTeacher == null) {
            return IrregularApplyResultDTO.builder()
                    .success(false)
                    .message("예약 가능한 강사가 없습니다.")
                    .build();
        }

        StudentEntity student = studentRepository.findById(request.getStudentNum()).orElse(null);
        String nickname = student != null ? student.getNickname() : "";
        String dayOfWeek = getEnglishDayOfWeek(classDate);
        String typeStr = request.getType().equals("free") ? "free" : "additional";
        String className = nickname + " " + start + "~" + end + " " + dayOfWeek + " " + typeStr;

        // zoom_meeting_id 구하기
        String zoomMeetingId = null;
        TeacherEntity teacher = teacherRepository.findById(assignedTeacher).orElse(null);
        if (teacher != null && teacher.getZoomNum() != null) {
            ZoomAccountEntity zoomAccount = zoomAccountRepository.findByZoomNum(teacher.getZoomNum()).orElse(null);
            if (zoomAccount != null) {
                zoomMeetingId = zoomAccount.getZoomId();
            }
        }

        DayClassEntity dayClass = new DayClassEntity();
        dayClass.setStudentNum(request.getStudentNum());
        dayClass.setTeacherNum(assignedTeacher);
        dayClass.setClassType(request.getType());
        dayClass.setClassDate(classDate);
        dayClass.setStartTime(LocalTime.parse(start + ":00"));
        dayClass.setEndTime(LocalTime.parse(end + ":00"));
        dayClass.setClassNum(null);
        dayClass.setClassName(className);

        dayClass.setZoomLink(null);
        dayClass.setZoomMeetingId(zoomMeetingId);
        dayClass.setAttendance(0);
        dayClass.setAbsent(0);
        dayClass.setComment(null);
        dayClass.setProgress(null);

        dayClassRepository.save(dayClass);

        return IrregularApplyResultDTO.builder()
                .success(true)
                .message("예약이 완료되었습니다.")
                .reservedTime(request.getTimeLabel())
                .teacherNickname("강사번호: " + assignedTeacher)
                .build();
    }

    private String getEnglishDayOfWeek(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY:    return "Monday";
            case TUESDAY:   return "Tuesday";
            case WEDNESDAY: return "Wednesday";
            case THURSDAY:  return "Thursday";
            case FRIDAY:    return "Friday";
            case SATURDAY:  return "Saturday";
            case SUNDAY:    return "Sunday";
        }
        return "";
    }

    private String getWeekDayStringJapanese(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case MONDAY:    return "月";
            case TUESDAY:   return "火";
            case WEDNESDAY: return "水";
            case THURSDAY:  return "木";
            case FRIDAY:    return "金";
            case SATURDAY:  return "土";
            case SUNDAY:    return "日";
        }
        return "";
    }
}

package com.english.lms.controller;

import com.english.lms.entity.StudentEntity;
import com.english.lms.entity.DayClassEntity;
import com.english.lms.entity.DayOffEntity;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.PointEntity;
import com.english.lms.dto.DayClassDTO;
import com.english.lms.dto.DayRowDto;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.DayOffRepository;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.PointRepository;
import com.english.lms.service.StudentMyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StudentMyPageController {

    private final StudentRepository studentRepository;
    private final DayClassRepository dayClassRepository;
    private final DayOffRepository dayOffRepository;
    private final TeacherRepository teacherRepository;
    private final PointRepository pointRepository;   // ★ 추가
    private final StudentMyPageService studentMyPageService;

    @GetMapping("/student/mypage")
    public String mypage(
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model
    ) {
        String loginId = getLoginId();
        StudentEntity student = studentRepository.findByStudentId(loginId)
                .orElseThrow(() -> new RuntimeException("学生情報がありません。"));

        YearMonth ym = (month != null) ? month : YearMonth.now();
        model.addAttribute("currentMonth", ym.toString());
        model.addAttribute("currentMonthLabel", ym.getYear() + "年" + ym.getMonthValue() + "月");
        model.addAttribute("prevMonth", ym.minusMonths(1).toString());
        model.addAttribute("nextMonth", ym.plusMonths(1).toString());

        // 오늘 날짜를 model에 추가
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);

        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();
        List<DayOffEntity> holidayEntities = dayOffRepository.findByOffDateBetween(
                java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)
        );
        Map<LocalDate, String> holidayMap = new HashMap<>();
        for (DayOffEntity e : holidayEntities) {
            holidayMap.put(e.getOffDate().toLocalDate(), e.getName());
        }

        DateTimeFormatter dayLabelFormatter = DateTimeFormatter.ofPattern("d");
        List<DayRowDto> rowList = new ArrayList<>();

        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            boolean isHoliday = holidayMap.containsKey(date);
            String holidayName = isHoliday ? holidayMap.get(date) : "";

            int dayOfWeek = date.getDayOfWeek().getValue(); // 月=1, ... 日=7
            String dateClass;
            if (isHoliday) {
                dateClass = "day-holiday";
            } else if (dayOfWeek == 7) {
                dateClass = "day-sunday";
            } else if (dayOfWeek == 6) {
                dateClass = "day-saturday";
            } else {
                dateClass = "day-normal";
            }

            List<DayClassEntity> lessonEntities = dayClassRepository.findByStudentNumAndClassDate(student.getStudentNum(), date);
            List<DayClassDTO> lessons = lessonEntities.stream()
                    .map(entity -> {
                        String teacherNickname = "";
                        if (entity.getTeacherNum() != null) {
                            Optional<TeacherEntity> t = teacherRepository.findById(entity.getTeacherNum());
                            teacherNickname = t.map(TeacherEntity::getNickname).orElse("");
                        }
                        return DayClassDTO.from(entity, teacherNickname);
                    })
                    .collect(Collectors.toList());

            int rowspan = lessons.size() > 0 ? lessons.size() : 1;

            if (lessons.isEmpty()) {
                // 授業のない日
                DayRowDto row = new DayRowDto();
                row.setDate(date);
                row.setDayLabel(date.format(dayLabelFormatter));
                row.setDateClass(dateClass);
                row.setHoliday(isHoliday);
                row.setHolidayName(holidayName);
                row.setLesson(null);
                row.setFirst(true);
                row.setRowspan(1);
                rowList.add(row);
            } else {
                for (int i = 0; i < lessons.size(); i++) {
                    DayClassDTO lesson = lessons.get(i);
                    DayRowDto row = new DayRowDto();
                    if (i == 0) {
                        row.setDate(date);
                        row.setDayLabel(date.format(dayLabelFormatter));
                        row.setDateClass(dateClass);
                        row.setHoliday(isHoliday);
                        row.setHolidayName(holidayName);
                        row.setFirst(true);
                        row.setRowspan(rowspan);
                    } else {
                        row.setDate(null);
                        row.setDayLabel(null);
                        row.setDateClass(null);
                        row.setHoliday(false);
                        row.setHolidayName(null);
                        row.setFirst(false);
                        row.setRowspan(0);
                    }
                    row.setLesson(lesson);
                    rowList.add(row);
                }
            }
        }
        model.addAttribute("rowList", rowList);

        // ▼▼▼▼▼ 포인트 관련: freePoint/additionalPoint 계산 및 모델 등록 ▼▼▼▼▼
        Integer studentNum = student.getStudentNum();

        // 무료포인트 (type='free', 여러 row면 합계)
        int freePoint = pointRepository
            .findByStudentNumAndType(studentNum, "free")
            .stream()
            .mapToInt(PointEntity::getPointAmount)
            .sum();

        // 보강포인트 (type='additional', expiresAt가 오늘 이후인 row만 합계)
        int additionalPoint = pointRepository
            .findByStudentNumAndType(studentNum, "additional")
            .stream()
            .filter(p -> p.getExpiresAt() != null && !p.getExpiresAt().isBefore(today))
            .mapToInt(PointEntity::getPointAmount)
            .sum();

        model.addAttribute("freePoint", freePoint);
        model.addAttribute("additionalPoint", additionalPoint);
        // ▲▲▲▲▲ 여기까지 추가 ▲▲▲▲▲

        return "student/mypage";
    }

    // 결석처리 API
    @PostMapping("/student/mypage/absent")
    @ResponseBody
    public String setAbsent(@RequestParam("dayClassNum") Integer dayClassNum) {
        studentMyPageService.setAbsent(dayClassNum, true);
        return "OK";
    }

    // 결석취소 API
    @PostMapping("/student/mypage/absent-cancel")
    @ResponseBody
    public String cancelAbsent(@RequestParam("dayClassNum") Integer dayClassNum) {
        studentMyPageService.setAbsent(dayClassNum, false);
        return "OK";
    }

    // 출석처리 API (참가버튼용)
    @PostMapping("/student/mypage/attend")
    @ResponseBody
    public String setAttendance(@RequestParam("dayClassNum") Integer dayClassNum) {
        studentMyPageService.setAttendance(dayClassNum, true);
        return "OK";
    }

    private String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }
}

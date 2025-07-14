package com.english.lms.controller;

import com.english.lms.entity.StudentEntity;
import com.english.lms.entity.DayClassEntity;
import com.english.lms.entity.DayOffEntity;
import com.english.lms.dto.DayDto;
import com.english.lms.dto.DayClassDto;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.DayOffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class StudentMyPageController {

    private final StudentRepository studentRepository;
    private final DayClassRepository dayClassRepository;
    private final DayOffRepository dayOffRepository;

    @GetMapping("/student/mypage")
    public String mypage(
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            Model model
    ) {
        // 1. 현재 로그인한 학생
        String loginId = getLoginId();
        StudentEntity student = studentRepository.findByStudentId(loginId)
                .orElseThrow(() -> new RuntimeException("학생 정보가 없습니다."));

        // 2. 월 정보 (없으면 이번달)
        YearMonth ym = (month != null) ? month : YearMonth.now();
        model.addAttribute("currentMonth", ym.toString());
        model.addAttribute("currentMonthLabel", ym.getYear() + "年" + ym.getMonthValue() + "月");
        model.addAttribute("prevMonth", ym.minusMonths(1).toString());
        model.addAttribute("nextMonth", ym.plusMonths(1).toString());

        // 3. DB에서 해당 월의 공휴일 목록 조회 (한 번에)
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();
        List<DayOffEntity> holidayEntities = dayOffRepository.findByOffDateBetween(
                java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate)
        );
        // Map<LocalDate, String>
        Map<LocalDate, String> holidayMap = new HashMap<>();
        for (DayOffEntity e : holidayEntities) {
            holidayMap.put(e.getOffDate().toLocalDate(), e.getName());
        }

        // 날짜 라벨 포맷
        DateTimeFormatter dayLabelFormatter = DateTimeFormatter.ofPattern("d");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // 4. 일별로 DayDto 생성
        List<DayDto> dayList = new ArrayList<>();
        for (int day = 1; day <= ym.lengthOfMonth(); day++) {
            LocalDate date = ym.atDay(day);
            boolean isHoliday = holidayMap.containsKey(date);
            String holidayName = isHoliday ? holidayMap.get(date) : "";

            // 주말 여부 계산
            DayOfWeek dow = date.getDayOfWeek();
            boolean isWeekend = (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY);

            // 학생의 해당 날짜 수업 목록 가져오기
            List<DayClassEntity> lessonEntities = dayClassRepository.findByStudentNumAndClassDate(student.getStudentNum(), date);

            // DayClassEntity → DayClassDto 변환
            List<DayClassDto> lessons = lessonEntities.stream()
                    .map(DayClassDto::from)
                    .collect(Collectors.toList());

            // 수업시간 문자열 조합
            String lessonTimesStr = lessons.stream()
                    .filter(l -> l.getStartTime() != null && l.getEndTime() != null)
                    .map(l -> l.getStartTime().format(timeFormatter) + "～" + l.getEndTime().format(timeFormatter))
                    .collect(Collectors.joining(", "));

            // 수업타입 문자열 조합
            String classTypesStr = lessons.stream()
                    .map(DayClassDto::getClassType)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", "));

            DayDto dto = new DayDto();
            dto.setDate(date);
            dto.setDayLabel(date.format(dayLabelFormatter)); // "11" 등
            dto.setHoliday(isHoliday);
            dto.setHolidayName(holidayName);
            dto.setLessons(lessons);
            dto.setLessonTimesStr(lessonTimesStr);
            dto.setClassTypesStr(classTypesStr);
            dto.setWeekend(isWeekend); // ★ 주말여부 세팅

            dayList.add(dto);
        }
        model.addAttribute("dayList", dayList);

        return "student/mypage";
    }

    // 로그인한 아이디
    private String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }
}

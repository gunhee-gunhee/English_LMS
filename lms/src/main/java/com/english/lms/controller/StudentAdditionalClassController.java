package com.english.lms.controller;

import com.english.lms.entity.PointEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.repository.DayOffRepository;
import com.english.lms.repository.PointRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.dto.IrregularApplyRequestDTO;
import com.english.lms.dto.IrregularApplyResultDTO;
import com.english.lms.service.StudentIrregularApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class StudentAdditionalClassController {

    private final PointRepository pointRepository;
    private final StudentRepository studentRepository;
    private final DayOffRepository dayOffRepository;
    private final StudentIrregularApplyService irregularApplyService;

    // 보강 신청 페이지
    @GetMapping("/student/additional-class-apply")
    public String showAdditionalClassApplyPage(
            Model model,
            @RequestParam(value = "applied", required = false) String applied
    ) {
        String loginId = getLoginId();

        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) {
            return "redirect:/student/login";
        }
        StudentEntity student = optStudent.get();

        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 보강 포인트 불러오기 (만료일 빠른 순)
        List<PointEntity> points = pointRepository
                .findByStudentNumAndTypeAndPointAmountGreaterThanEqualAndExpiresAtGreaterThanEqualOrderByExpiresAtAsc(
                        student.getStudentNum(),
                        "additional",
                        1,
                        today
                );

        List<Map<String, Object>> pointOptions = new ArrayList<>();
        for (PointEntity point : points) {
            Map<String, Object> map = new HashMap<>();
            map.put("pointNum", point.getPointNum());
            StringBuilder label = new StringBuilder();
            if (point.getAbsentDate() != null) {
                label.append(point.getAbsentDate().format(dateFormatter));
            }
            label.append(" 欠席ポイント (数量: ")
                    .append(point.getPointAmount())
                    .append(") (有効期限: ");
            if (point.getExpiresAt() != null) {
                label.append(point.getExpiresAt().format(dateFormatter));
            }
            label.append(")");
            map.put("label", label.toString());
            pointOptions.add(map);
        }
        model.addAttribute("pointOptions", pointOptions);

        // 공휴일 정보
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        var dayOffList = dayOffRepository.findByOffDateBetween(
                java.sql.Date.valueOf(start),
                java.sql.Date.valueOf(end)
        );
        List<Map<String, String>> dayOffInfo = new ArrayList<>();
        for (var d : dayOffList) {
            Map<String, String> m = new HashMap<>();
            m.put("date", d.getOffDate().toLocalDate().toString());
            m.put("name", d.getName());
            dayOffInfo.add(m);
        }
        model.addAttribute("dayOffList", dayOffInfo);

        // 신청완료시 메시지
        if ("1".equals(applied)) {
            model.addAttribute("successMessage", "申請が完了しました。");
        }

        return "student/additional-class-apply";
    }

    // 신청 가능한 시간 조회 API (AJAX)
    @GetMapping("/api/additional-class-available-times")
    @ResponseBody
    public List<com.english.lms.dto.IrregularAvailableTimeDTO> getAvailableTimes(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return irregularApplyService.getAvailableTimes(date);
    }

    // 보강 신청 (포인트 차감, 배정)
    @PostMapping("/student/additional-class/apply")
    public String postApply(
            @RequestParam("pointNum") Integer pointNum,
            @RequestParam("selectedDate") String selectedDate,
            @RequestParam("selectedTime") String selectedTime,
            Model model
    ) {
        String loginId = getLoginId();

        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) {
            return "redirect:/student/login";
        }
        StudentEntity student = optStudent.get();

        // 포인트 사용 처리
        PointEntity point = pointRepository.findById(pointNum).orElse(null);
        if (point != null && point.getPointAmount() > 0) {
            point.setPointAmount(point.getPointAmount() - 1);
            if (point.getPointAmount() == 0) {
                pointRepository.delete(point);
            } else {
                pointRepository.save(point);
            }
        }

        // 서비스 호출: 실제 수업 예약 처리 (배정)
        IrregularApplyRequestDTO request = IrregularApplyRequestDTO.builder()
                .studentNum(student.getStudentNum())
                .date(selectedDate)
                .timeLabel(selectedTime)
                .type("additional") // 보강
                .build();
        IrregularApplyResultDTO result = irregularApplyService.applyIrregularClass(request);

        if (result.isSuccess()) {
            return "redirect:/student/additional-class-apply?applied=1";
        } else {
            model.addAttribute("errorMessage", result.getMessage());
            return "student/additional-class-apply";
        }
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

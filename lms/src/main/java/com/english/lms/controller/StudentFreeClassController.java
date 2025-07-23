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
import java.util.*;

@Controller
@RequiredArgsConstructor
public class StudentFreeClassController {

    private final PointRepository pointRepository;
    private final StudentRepository studentRepository;
    private final DayOffRepository dayOffRepository;
    private final StudentIrregularApplyService irregularApplyService;

    // 무료수업 신청 페이지
    @GetMapping("/student/free-class-apply")
    public String showFreeClassApplyPage(
            Model model,
            @RequestParam(value = "applied", required = false) String applied
    ) {
        String loginId = getLoginId();

        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) {
            return "redirect:/student/login";
        }
        StudentEntity student = optStudent.get();

        // [★변경] 무료수업 포인트: 만료일, absentDate 조건 없이
        List<PointEntity> points = pointRepository
                .findByStudentNumAndTypeAndPointAmountGreaterThanEqual(
                        student.getStudentNum(),
                        "free",
                        1
                );

        List<Map<String, Object>> pointOptions = new ArrayList<>();
        for (PointEntity point : points) {
            Map<String, Object> map = new HashMap<>();
            map.put("pointNum", point.getPointNum());
            // [★변경] 무료포인트는 label 단순하게
            String label = "無料授業ポイント (数量: " + point.getPointAmount() + ")";
            map.put("label", label);
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

        return "student/free-class-apply";
    }

    // 신청 가능한 시간 조회 API (AJAX)
    @GetMapping("/api/free-class-available-times")
    @ResponseBody
    public List<com.english.lms.dto.IrregularAvailableTimeDTO> getAvailableTimes(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return irregularApplyService.getAvailableTimes(date);
    }

    // 무료수업 신청 (포인트 차감, 배정)
    @PostMapping("/student/free-class/apply")
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
                .type("free")
                .build();
        IrregularApplyResultDTO result = irregularApplyService.applyIrregularClass(request);

        if (result.isSuccess()) {
            return "redirect:/student/free-class-apply?applied=1";
        } else {
            model.addAttribute("errorMessage", result.getMessage());
            return "student/free-class-apply";
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

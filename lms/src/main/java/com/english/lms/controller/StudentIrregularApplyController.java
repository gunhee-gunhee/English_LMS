package com.english.lms.controller;

import com.english.lms.dto.IrregularAvailableTimeDTO;
import com.english.lms.dto.IrregularApplyRequestDTO;
import com.english.lms.dto.IrregularApplyResultDTO;
import com.english.lms.entity.PointEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.repository.PointRepository;
import com.english.lms.repository.StudentRepository;
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
public class StudentIrregularApplyController {

    private final StudentIrregularApplyService irregularApplyService;
    private final PointRepository pointRepository;
    private final StudentRepository studentRepository;

    // 예약 가능한 시간 AJAX API
    @ResponseBody
    @GetMapping("/api/irregular-available-times")
    public List<IrregularAvailableTimeDTO> getAvailableTimes(@RequestParam("date") String date) {
        return irregularApplyService.getAvailableTimes(LocalDate.parse(date));
    }

    // 예약 신청 페이지 (보강, 무료 모두 공통화/분리 가능)
    @GetMapping("/student/irregular-class-apply")
    public String showIrregularClassApplyPage(
            Model model,
            @RequestParam(value = "type", required = false, defaultValue = "makeup") String type,
            @RequestParam(value = "applied", required = false) String applied
    ) {
        String loginId = getLoginId();
        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) return "redirect:/student/login";
        StudentEntity student = optStudent.get();

        // 포인트 불러오기 (보강만)
        if ("makeup".equals(type)) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<PointEntity> points = pointRepository
                    .findByStudentNumAndTypeAndPointAmountGreaterThanEqualAndExpiresAtGreaterThanEqualOrderByExpiresAtAsc(
                            student.getStudentNum(), "additional", 1, today);
            List<Map<String, Object>> pointOptions = new ArrayList<>();
            for (PointEntity point : points) {
                Map<String, Object> map = new HashMap<>();
                map.put("pointNum", point.getPointNum());
                StringBuilder label = new StringBuilder();
                if (point.getAbsentDate() != null) {
                    label.append(point.getAbsentDate().format(dateFormatter));
                }
                label.append(" 欠席ポイント (数量: ").append(point.getPointAmount()).append(") (有効期限: ");
                if (point.getExpiresAt() != null) {
                    label.append(point.getExpiresAt().format(dateFormatter));
                }
                label.append(")");
                map.put("label", label.toString());
                pointOptions.add(map);
            }
            model.addAttribute("pointOptions", pointOptions);
        }

        model.addAttribute("type", type);
        if ("1".equals(applied)) model.addAttribute("successMessage", "申請が完了しました。");
        return "student/irregular-class-apply";
    }

    // 예약 신청 POST (보강/무료 통합)
    @PostMapping("/student/irregular-class-apply")
    public String postApply(
            @RequestParam(value = "pointNum", required = false) Integer pointNum,
            @RequestParam("selectedDate") String selectedDate,
            @RequestParam("selectedTime") String selectedTime,
            @RequestParam("type") String type
    ) {
        String loginId = getLoginId();
        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) return "redirect:/student/login";
        StudentEntity student = optStudent.get();

        // 포인트 차감(보강)
        if ("makeup".equals(type) && pointNum != null) {
            PointEntity point = pointRepository.findById(pointNum).orElse(null);
            if (point != null && point.getPointAmount() > 0) {
                point.setPointAmount(point.getPointAmount() - 1);
                if (point.getPointAmount() == 0) {
                    pointRepository.delete(point);
                } else {
                    pointRepository.save(point);
                }
            }
        }

        // 예약 처리
        IrregularApplyRequestDTO req = new IrregularApplyRequestDTO();
        req.setStudentNum(student.getStudentNum());
        req.setDate(selectedDate);
        req.setTimeLabel(selectedTime);
        req.setType(type);

        irregularApplyService.applyIrregularClass(req);

        return "redirect:/student/irregular-class-apply?type=" + type + "&applied=1";
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

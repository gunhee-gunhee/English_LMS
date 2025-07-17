package com.english.lms.advice;

import com.english.lms.entity.PointEntity;
import com.english.lms.entity.StudentEntity;
import com.english.lms.repository.PointRepository;
import com.english.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.time.LocalDate;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class StudentPointAdvice {

    private final StudentRepository studentRepository;
    private final PointRepository pointRepository;

    @ModelAttribute
    public void addCommonPoints(Model model) {
        String loginId = getLoginId();
        if (loginId == null || loginId.equals("anonymousUser")) return;

        StudentEntity student = studentRepository.findByStudentId(loginId)
                .orElse(null);
        if (student == null) return;

        Integer studentNum = student.getStudentNum();
        LocalDate today = LocalDate.now();

        // 무료포인트 (type='free' 전부 합)
        int freePoint = pointRepository
                .findByStudentNumAndType(studentNum, "free")
                .stream()
                .mapToInt(PointEntity::getPointAmount)
                .sum();

        // 보강포인트 (type='additional', 만료일 안 지난 것 합)
        int additionalPoint = pointRepository
                .findByStudentNumAndType(studentNum, "additional")
                .stream()
                .filter(p -> p.getExpiresAt() != null && !p.getExpiresAt().isBefore(today))
                .mapToInt(PointEntity::getPointAmount)
                .sum();

        model.addAttribute("freePoint", freePoint);
        model.addAttribute("additionalPoint", additionalPoint);
        model.addAttribute("studentName", student.getNickname());
    }

    private String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }
}

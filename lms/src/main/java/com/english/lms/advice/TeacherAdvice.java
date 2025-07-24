package com.english.lms.advice;

import com.english.lms.entity.TeacherEntity;
import com.english.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class TeacherAdvice {

    private final TeacherRepository teacherRepository;

    @ModelAttribute
    public void addTeacherNickname(Model model) {
        String loginId = getLoginId();
        if (loginId == null || loginId.equals("anonymousUser")) return;

        TeacherEntity teacher = teacherRepository.findByTeacherId(loginId)
                .orElse(null);
        if (teacher == null) return;

        model.addAttribute("teacherNickname", teacher.getNickname());
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

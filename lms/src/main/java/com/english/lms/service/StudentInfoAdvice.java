package com.english.lms.service;

import com.english.lms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class StudentInfoAdvice {

    @Autowired
    private StudentRepository studentRepository;

    @ModelAttribute("studentName")
    public String addStudentNameToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return "";
        String loginId;
        if (authentication.getPrincipal() instanceof UserDetails) {
            loginId = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            loginId = authentication.getPrincipal().toString();
        }
        return studentRepository.findByStudentId(loginId)
                .map(entity -> entity.getNickname())
                .orElse("");
    }
}

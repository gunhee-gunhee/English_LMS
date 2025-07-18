package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentFreeClassController {

    @GetMapping("/student/free-class-apply")
    public String showAdditionalClassApplyPage() {
        // templates/student/additional-class-apply.html을 렌더링
        return "student/free-class-apply";
    }
}
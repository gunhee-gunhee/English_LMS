package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentEmailController {

    // 여기 추가! email-check.html로 이동시켜주는 메서드
    @GetMapping("/student/email-check")
    public String emailCheckPage() {
        return "student/email-check";
        // 즉, templates/student/email-check.html 파일을 렌더링
    }
}

package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentIrregularClassController {

    // 여기 추가! email-check.html로 이동시켜주는 메서드
    @GetMapping("/student/irregular-class-apply")
    public String emailCheckPage() {
        return "student/irregular-class-apply";
        // 즉, templates/student/email-check.html 파일을 렌더링
    }
}

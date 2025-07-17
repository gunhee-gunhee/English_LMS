package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StudentIrregularClassController {

    // 불필요한 주석/설명 정리 및 파라미터 반영
    @GetMapping("/student/irregular-class-apply")
    public String irregularClassApply(
            @RequestParam(value = "type", required = false) String type,
            Model model) {
        // type 파라미터가 "free"일 경우에만 안내문구를 model에 넣음
        if ("free".equals(type)) {
            model.addAttribute("applyTitle", "無料授業申請");
        }
        
        if ("additional".equals(type)) {
            model.addAttribute("applyTitle", "補講申請");
        } 
        // student/irregular-class-apply.html을 렌더링
        return "student/irregular-class-apply";
    }
}

package com.english.lms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/student")
public class StudentController {

    /**
     * 学生詳細画面を表示するだけのGETコントローラ
     */
    @GetMapping("/detail")
    public String showStudentDetail(Model model) {
        log.info("学生詳細画面を表示します");

        // 必要に応じて model.addAttribute(...) でデータを渡せます
        return "admin/student-detail";  // templates/admin/student-detail.html
    }
}

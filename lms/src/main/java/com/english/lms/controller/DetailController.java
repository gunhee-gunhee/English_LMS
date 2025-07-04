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
@RequestMapping("admin")
public class DetailController {

    /**
     * 学生詳細画面を表示するだけのGETコントローラ
     */
    @GetMapping("/student/detail")
    public String showStudentDetail(Model model) {
        log.info("学生詳細画面を表示します");

        // 必要に応じて model.addAttribute(...) でデータを渡せます
        return "admin/student-detail";  // templates/admin/student-detail.html
    }
    
    @GetMapping("/teacher/detail")
    public String showTeacherDetail(Model model) {
        log.info("先生詳細画面を表示します");

        // 必要に応じて model.addAttribute(...) でデータを渡せます
        return "admin/teacher-detail";  // templates/admin/teacher-detail.html
    }
    
    @GetMapping("/admin/detail")
    public String showAdminDetail(Model model) {
        log.info("管理者詳細画面を表示します");

        // 必要に応じて model.addAttribute(...) でデータを渡せます
        return "admin/admin-detail";  // templates/admin/admin-detail.html
    }
}

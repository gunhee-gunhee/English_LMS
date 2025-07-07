package com.english.lms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.english.lms.dto.StudentDetailDto;
import com.english.lms.service.AdminStudentService;

/**
 * 学生詳細画面コントローラー（管理者用）
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DetailController {

    private final AdminStudentService adminStudentService;

    /**
     * 学生詳細画面を表示するGET
     */
    @GetMapping("/student/detail/{studentNum}")
    public String showStudentDetail(@PathVariable Integer studentNum, Model model) {
        log.info("学生詳細ページを表示: {}", studentNum);

        // 学生詳細情報を取得
        StudentDetailDto student = adminStudentService.getStudentDetail(studentNum);
        model.addAttribute("student", student);

        // 年齢・英語レベル・講師・曜日・時間などの選択肢
        model.addAttribute("ageList", adminStudentService.getAgeOptions());
        model.addAttribute("englishLevelList", adminStudentService.getEnglishLevelOptions());
        model.addAttribute("teacherList", adminStudentService.getTeacherOptions());
        model.addAttribute("dayList", adminStudentService.getWeekDayOptions());
        model.addAttribute("timeList", adminStudentService.getTimeOptions(studentNum));

        return "admin/student-detail";
    }

    /**
     * 学生情報を修正するPOST
     */
    @PostMapping("/student/detail/{studentNum}")
    public String updateStudent(@PathVariable Integer studentNum,
                                @ModelAttribute StudentDetailDto dto,
                                Model model) {
        adminStudentService.updateStudent(studentNum, dto);
        return "redirect:/admin/student/detail/" + studentNum + "?success";
    }

    /**
     * 学生を削除するPOST
     */
    @PostMapping("/student/detail/{studentNum}/delete")
    public String deleteStudent(@PathVariable Integer studentNum) {
        adminStudentService.deleteStudent(studentNum);
        return "redirect:/admin/student-list?deleted";
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

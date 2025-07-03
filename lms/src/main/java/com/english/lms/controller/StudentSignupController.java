package com.english.lms.controller;

import com.english.lms.dto.StudentDTO;
import com.english.lms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class StudentSignupController {

    private final StudentService studentService;

    // GET: 회원가입 폼
    @GetMapping("/student/signup")
    public String signupPage(Model model) {
        if (!model.containsAttribute("studentDTO")) {
            model.addAttribute("studentDTO", new StudentDTO());
        }
        return "student/signup";
    }
    
    @GetMapping("/student/signup-complete")
    public String signupCompletePage(Model model) {
        return "student/signup-complete";
    }

    // POST: 회원가입 처리
    @PostMapping("/student/signup")
    public String signup(@ModelAttribute StudentDTO studentDTO, RedirectAttributes redirectAttributes) {
        // 비밀번호 체크
        if (!studentDTO.getPassword().equals(studentDTO.getPasswordCheck())) {
            redirectAttributes.addFlashAttribute("error", "パスワードが一致しません。");
            redirectAttributes.addFlashAttribute("studentDTO", studentDTO);
            return "redirect:/student/signup";
        }
        // 중복 ID 체크
        if (studentService.existsById(studentDTO.getId())) {
            redirectAttributes.addFlashAttribute("error", "このIDは既に登録されています。");
            redirectAttributes.addFlashAttribute("studentDTO", studentDTO);
            return "redirect:/student/signup";
        }
        // 실제 등록
        studentService.registerStudent(studentDTO);

        // 성공 시 메시지 없이 로그인 페이지로 이동
        return "redirect:/student/signup-complete";
    }
}

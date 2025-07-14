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
    public String signupPage(
            @RequestParam(value = "id", required = false) String id, // <-- 여기 수정 (email → id)
            Model model
    ) {
        // 기존에 에러 등으로 넘어온 값이 있으면 사용
        StudentDTO studentDTO;
        if (model.containsAttribute("studentDTO")) {
            studentDTO = (StudentDTO) model.asMap().get("studentDTO");
        } else {
            studentDTO = new StudentDTO();
        }
        // id 파라미터가 있으면 id에 세팅 (readonly로 입력됨)
        if (id != null && !id.isEmpty()) {
            studentDTO.setId(id);
        }
        model.addAttribute("studentDTO", studentDTO);

        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.asMap().get("error"));
        }
        return "student/signup";
    }

    // 회원가입 완료 페이지
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
            // 다시 signup으로 리다이렉트, id가 유지되게 파라미터로 전달
            return "redirect:/student/signup?id=" + studentDTO.getId();
        }
        // 중복 ID 체크
        if (studentService.existsById(studentDTO.getId())) {
            redirectAttributes.addFlashAttribute("error", "このIDは既に登録されています。");
            redirectAttributes.addFlashAttribute("studentDTO", studentDTO);
            return "redirect:/student/signup?id=" + studentDTO.getId();
        }
        // 실제 등록
        studentService.registerStudent(studentDTO);

        // 성공 시 메시지 없이 완료 페이지로 이동
        return "redirect:/student/signup-complete";
    }
}

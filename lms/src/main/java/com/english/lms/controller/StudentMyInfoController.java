package com.english.lms.controller;

import com.english.lms.entity.StudentEntity;
import com.english.lms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentMyInfoController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1) GET: 비밀번호 확인 페이지
    @GetMapping("/myinfocheck")
    public String myinfocheck() {
        return "student/myinfocheck";
    }

    // 2) POST: 비밀번호 확인 처리
    @PostMapping("/myinfocheck")
    public String myinfocheckProcess(@RequestParam("password") String password, Model model, HttpSession session) {
        String loginId = getLoginId();
        StudentEntity student = studentRepository.findByStudentId(loginId).orElse(null);
        if (student == null || !passwordEncoder.matches(password, student.getPassword())) {
            model.addAttribute("error", "パスワードが正しくありません。");
            return "student/myinfocheck";
        }
        // 비번 맞으면 세션에 인증 표시 후 myinfo로 리다이렉트
        session.setAttribute("infoVerified", true);
        return "redirect:/student/myinfo";
    }

    // 3) GET: 본인 정보 화면 (비밀번호 인증 확인 필요)
    @GetMapping("/myinfo")
    public String myinfo(Model model, HttpSession session) {
        // 비번 인증 확인 (없으면 myinfocheck로 이동)
        Boolean verified = (Boolean) session.getAttribute("infoVerified");
        if (verified == null || !verified) {
            return "redirect:/student/myinfocheck";
        }
        String loginId = getLoginId();
        StudentEntity student = studentRepository.findByStudentId(loginId).orElse(new StudentEntity());
        model.addAttribute("student", student);
        return "student/myinfo";
    }

    // 4) POST: 정보 변경 (수정/저장)
    @PostMapping("/myinfo")
    public String updateMyinfo(@ModelAttribute("student") StudentEntity formStudent,
                               @RequestParam(name = "password", required = false) String password,
                               @RequestParam(name = "passwordConfirm", required = false) String passwordConfirm,
                               Model model,
                               HttpSession session) {
        // (옵션) 비번인증 체크
        Boolean verified = (Boolean) session.getAttribute("infoVerified");
        if (verified == null || !verified) {
            return "redirect:/student/myinfocheck";
        }

        String loginId = getLoginId();
        StudentEntity student = studentRepository.findByStudentId(loginId).orElse(null);
        if (student == null) {
            model.addAttribute("error", "학생 정보가 존재하지 않습니다.");
            return "student/myinfo";
        }

        // 비밀번호 변경
        if (password != null && !password.isBlank() && password.equals(passwordConfirm)) {
            student.setPassword(passwordEncoder.encode(password));
        } else if (password != null && !password.isBlank()) {
            model.addAttribute("student", student);
            model.addAttribute("error", "パスワードと確認用パスワードが一致しません。");
            return "student/myinfo";
        }

        // 나머지 정보 수정
        student.setNickname(formStudent.getNickname());
        student.setNicknameJp(formStudent.getNicknameJp());
        student.setEnglishLevel(formStudent.getEnglishLevel());

        studentRepository.save(student);

        // 정보 수정 후 세션 인증값 삭제(다시 비밀번호 확인 필요하게)
        session.removeAttribute("infoVerified");

        model.addAttribute("student", student);
        model.addAttribute("success", "保存しました。");
        return "student/myinfo";
    }

    // 로그인한 아이디 가져오기 공통 메서드
    private String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }
}

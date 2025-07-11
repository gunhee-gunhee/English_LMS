package com.english.lms.controller;

import com.english.lms.entity.StudentEntity;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentInfoController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InfoRepository infoRepository;

    // 1) GET: パスワード確認ページ
    @GetMapping("/myinfocheck")
    public String myinfocheck() {
        return "student/myinfocheck";
    }

    // 2) POST: パスワード確認処理
    @PostMapping("/myinfocheck")
    public String myinfocheckProcess(@RequestParam("password") String password, Model model, HttpSession session) {
        String loginId = getLoginId();
        StudentEntity student = studentRepository.findByStudentId(loginId).orElse(null);
        if (student == null || !passwordEncoder.matches(password, student.getPassword())) {
            model.addAttribute("error", "パスワードが正しくありません。");
            return "student/myinfocheck";
        }
        // パスワード一致の場合、セッションに認証マークを付けて myinfo へリダイレクト
        session.setAttribute("infoVerified", true);
        return "redirect:/student/myinfo";
    }

    // 3) GET: 本人情報ページ（パスワード認証が必要）
    @GetMapping("/myinfo")
    public String myinfo(Model model, HttpSession session) {
        // パスワード認証が無ければ myinfocheck へ
        Boolean verified = (Boolean) session.getAttribute("infoVerified");
        if (verified == null || !verified) {
            return "redirect:/student/myinfocheck";
        }
        String loginId = getLoginId();
        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) {
            return "redirect:/student/login"; // 学生情報が無い場合はログインページへ
        }
        StudentEntity student = optStudent.get();
        model.addAttribute("student", student);
        return "student/myinfo";
    }

    // 4) POST: 情報更新（修正/保存）
    @PostMapping("/myinfo")
    public String updateMyinfo(@ModelAttribute("student") StudentEntity formStudent,
                               @RequestParam(name = "password", required = false) String password,
                               @RequestParam(name = "passwordConfirm", required = false) String passwordConfirm,
                               Model model,
                               HttpSession session) {
        // （オプション）パスワード認証チェック
        Boolean verified = (Boolean) session.getAttribute("infoVerified");
        if (verified == null || !verified) {
            return "redirect:/student/myinfocheck";
        }

        String loginId = getLoginId();
        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) {
            return "redirect:/student/login"; // 学生情報が無い場合はログインページへ
        }
        StudentEntity student = optStudent.get();

        // パスワード変更
        if (password != null && !password.isBlank() && password.equals(passwordConfirm)) {
            student.setPassword(passwordEncoder.encode(password));
        } else if (password != null && !password.isBlank()) {
            model.addAttribute("student", student);
            model.addAttribute("error", "パスワードと確認用パスワードが一致しません。");
            return "student/myinfo";
        }

        // その他の情報修正
        student.setNickname(formStudent.getNickname());
        student.setNicknameJp(formStudent.getNicknameJp());
        student.setEnglishLevel(formStudent.getEnglishLevel());

        studentRepository.save(student);

        // 情報修正後、セッション認証値削除（再度パスワード確認が必要に）
        session.removeAttribute("infoVerified");

        model.addAttribute("student", student);
        model.addAttribute("success", "保存しました。");
        return "student/myinfo";
    }

    // ---------- textinfo.html マッピングおよび教材情報の取得 ----------
    @GetMapping("/textinfo")
    public String textInfoPage(Model model) {
        // ログイン中の学生の student_num を取得
        String loginId = getLoginId();
        Optional<StudentEntity> optStudent = studentRepository.findByStudentId(loginId);
        if (optStudent.isEmpty()) {
            return "redirect:/student/login"; // 学生情報が無い場合はログインページへ
        }
        StudentEntity student = optStudent.get();

        // 教材リスト（名前のみ）を取得
        List<String> textbookList = infoRepository.findTextNamesByStudentNum(student.getStudentNum());
        model.addAttribute("textbookList", textbookList);
        return "student/textinfo";
    }

    // ログインしているユーザーIDの取得用共通メソッド
    private String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }
}

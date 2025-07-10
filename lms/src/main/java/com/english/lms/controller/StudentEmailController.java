package com.english.lms.controller;

import com.english.lms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Controller
@RequestMapping("/student")
public class StudentEmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StudentService studentService;

    // 인증번호, 발송 이메일 임시 저장 (실제 서비스는 세션/DB 사용 권장)
    private String authCode = null;
    private String sentEmail = null;

    @GetMapping("/email-check")
    public String showEmailCheckPage(Model model) {
        return "student/email-check";
    }

    @PostMapping("/email-check")
    public String sendEmail(
            @RequestParam("email") String email,
            Model model
    ) {
        // 1. 이미 등록된 이메일(아이디)인지 체크
        if (studentService.existsById(email)) {
            model.addAttribute("email", email);
            model.addAttribute("alreadyRegistered", true);
            return "student/email-check";
        }

        // 2. 미등록이면 인증코드 발송
        String code = generateAuthCode();
        this.authCode = code;
        this.sentEmail = email;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("メール認証コード");
        message.setText("認証コード: " + code + "\n\nこのコードを画面に入力してください。");
        message.setFrom("system@instabiz.jp");

        mailSender.send(message);

        model.addAttribute("email", email);
        model.addAttribute("sent", true);
        return "student/email-check";
    }

    // 인증번호 확인 후, 성공 시 회원가입으로 이동(이메일 파라미터 전달)/실패 시 안내문
    @PostMapping("/email-check/verify")
    public String verifyAuthCode(
            @RequestParam("email") String email,
            @RequestParam("authCode") String inputCode,
            Model model
    ) {
        boolean match = sentEmail != null && sentEmail.equals(email) && authCode != null && authCode.equals(inputCode);

        if (match) {
            // 인증 성공 시, 회원가입 화면으로 리다이렉트(이메일을 id 파라미터로 전달)
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
            return "redirect:/student/signup?id=" + encodedEmail;
            // ※ StudentController에서 @RequestParam("id")로 받을 것
        } else {
            // 인증 실패 시, 인증 실패 안내문구와 함께 인증화면 다시 표시
            model.addAttribute("email", email);
            model.addAttribute("sent", true);
            model.addAttribute("authFailed", true);
            return "student/email-check";
        }
    }

    private String generateAuthCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}

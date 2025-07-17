package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;

@Controller  // Controller -> View 반환 , RestController - 데이터 반환(JSON, XML..)
@RequiredArgsConstructor//생성자 의존성 주입 
public class MainController {

    @GetMapping("/")  
    public String main() {
        // "main" -> "redirect:/student/login" 으로 변경!
        return "redirect:/student/login"; // "/"로 들어오면 /student/login으로 리다이렉트
    }
}

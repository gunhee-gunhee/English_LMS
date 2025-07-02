package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentLoginController {

	@GetMapping("/student/login")
	public String loginPage() {
		return "student/login";
	}
	
	@GetMapping("/student/mypage")
	public String loginSuccessPage() {
		return "student/mypage";
	}
}

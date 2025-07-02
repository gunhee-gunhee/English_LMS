package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeacherLoginController {
	
	@GetMapping("/teacher/login")
	public String loginPage() {
		return "teacher/login";
	}
	
	@GetMapping("/teacher/mypage")
	public String loginSuccessPage(){
		return "teacher/mypage";
	}
	
}


package com.english.lms.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.english.lms.dto.CustomUserDetails;
import com.english.lms.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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


package com.english.lms.controller;

import com.english.lms.repository.StudentRepository;
import com.english.lms.entity.StudentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentMyPageController {

	@GetMapping("/student/mypage")
	public String loginSuccessPage() {
	    return "student/mypage";
	}
}

package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {
	
	@GetMapping("/admin/login")
	public String loginPage() {
		return "admin/login";
	}
	
	@GetMapping("/admin/regular-class-list")
	public String loginSuccessPage() {
		return "admin/regular-class-list";
	}
	
}

package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CenterLoginController {

	@GetMapping("/center/login")
	public String loginPage() {
		return "center/login";
	}
	

}

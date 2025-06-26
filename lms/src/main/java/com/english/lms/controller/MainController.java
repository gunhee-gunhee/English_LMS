package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller  // Controller -> View 반환 , RestController - 데이터 반환(JSON, XML..)
@RequiredArgsConstructor//생성자 의존성 주입 
public class MainController {

	@GetMapping("/")  
	public String main() {
		return "main"; //templates/main.html 을 반환함.
	
	}
}

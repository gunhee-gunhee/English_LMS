package com.english.lms.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.english.lms.dto.AdminDTO;
import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.service.ZoomAccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminRegisterController {
	
	private final ZoomAccountService zoomAccountService;

	@GetMapping("/admin/register")
	public String showPage(Model model) {
		model.addAttribute("studentDTO", new StudentDTO());
		model.addAttribute("teacherDTO", new TeacherDTO());
		model.addAttribute("adminDTO", new AdminDTO());	
		
		List<String> zoomIdList = zoomAccountService.getAllZoomIds();
		model.addAttribute("zoomIdList", zoomIdList);
		
		return "admin/register";
	}
	
	
	
}

package com.english.lms.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.english.lms.dto.StudentDTO;
import com.english.lms.service.StudentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentListController {
	
	private final StudentService studentService;

//	@GetMapping("/admin/student-list")
//	public String studentListPage(Model model) {
//		
//		
//		List<StudentDTO> students = studentService.getAllStudentWithTeacher();
//		
//		model.addAttribute("students", students);
//		
//		return "admin/student-list";
//	}
	
	@GetMapping("/admin/student-list")
	public String studentListPage(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size",defaultValue = "10") int size,
			@RequestParam(name = "sort", defaultValue = "studentId") String sort,
			@RequestParam(name = "dir", defaultValue = "asc") String dir,
			Model model) {
		
		Page<StudentDTO> studentPage = studentService.getStudentPageWithTeacher(PageRequest.of(page, size, Sort.by("studentId").ascending()));
		
		model.addAttribute("students", studentPage.getContent());
		model.addAttribute("totalPages", studentPage.getTotalPages());
		model.addAttribute("currentPage", page);
		
		return "admin/student-list";
		
	}
	
	
}

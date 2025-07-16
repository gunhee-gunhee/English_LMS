package com.english.lms.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		
		  System.out.println("size = " + size);
		// ソート条件と並び順は Pageable で構成
		Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
	    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

	    Page<StudentDTO> studentPage = studentService.getStudentPageWithTeacher(pageable);

	    model.addAttribute("studentPage", studentPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        
		return "admin/student-list";
		
	}
	
	
}

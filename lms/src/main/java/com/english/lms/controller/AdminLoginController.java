package com.english.lms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.english.lms.dto.StudentDTO;
import com.english.lms.service.AdminStudentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminLoginController {
	
	@GetMapping("/admin/login")
	public String loginPage() {
		return "admin/login";
	}
	
//	@GetMapping("/admin/regular-class-list")
//	public String loginSuccessPage() {
//		return "admin/regular-class-list";
//	}
	
//	private final AdminStudentService adminStudentService;

	
//	@GetMapping("/admin/student-list")
//	public String studentListPage(
//			@RequestParam(name = "page", defaultValue = "0") int page,
//			@RequestParam(name = "size",defaultValue = "10") int size,
//			@RequestParam(name = "sort", defaultValue = "studentId") String sort,
//			@RequestParam(name = "dir", defaultValue = "asc") String dir,
//			Model model) {
//		
//		// ソート条件と並び順は Pageable で構成
//		// dir に渡された値が大文字・小文字を区別せず "desc" と一致すれば true！
//		Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//	    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
//
//	    Page<StudentDTO> studentPage = adminStudentService.getStudentPageWithTeacher(pageable);
//
//	    model.addAttribute("studentPage", studentPage);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("size", size);
//        model.addAttribute("sort", sort);
//        model.addAttribute("dir", dir);
//        
//		return "admin/student-list";
//		
//	}
	
}

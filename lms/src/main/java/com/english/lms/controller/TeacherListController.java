package com.english.lms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.service.TeacherService;

import lombok.RequiredArgsConstructor;

	@Controller
	@RequiredArgsConstructor
	public class TeacherListController {
	
		private final TeacherService teacherService;
		
		@GetMapping("/admin/teacher-list")
		public String studentListPage(
				@RequestParam(name = "page", defaultValue = "0") int page,
				@RequestParam(name = "size",defaultValue = "10") int size,
				@RequestParam(name = "sort", defaultValue = "studentId") String sort,
				@RequestParam(name = "dir", defaultValue = "asc") String dir,
				Model model) {
			
			Page<TeacherDTO> teacherPage = teacherService.getTeacherPage(PageRequest.of(page, size, Sort.by("nickname").ascending()));
			
			model.addAttribute("teacher", teacherPage.getContent());
			model.addAttribute("totalPages", teacherPage.getTotalPages());
			model.addAttribute("currentPage", page);
			
			return "admin/teacher-list";
			
	}
	
}

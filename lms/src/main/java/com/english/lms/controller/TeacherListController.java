package com.english.lms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.english.lms.dto.TeacherDTO;
import com.english.lms.service.TeacherService;

import lombok.RequiredArgsConstructor;

	@Controller
	@RequiredArgsConstructor
	public class TeacherListController {
	
		private final TeacherService teacherService;
		
		@GetMapping("/admin/teacher-list")
		public String teacherListPage(
				@RequestParam(name = "page", defaultValue = "0") int page,
				@RequestParam(name = "size",defaultValue = "10") int size,
				@RequestParam(name = "sort", defaultValue = "teacherId") String sort,
				@RequestParam(name = "dir", defaultValue = "asc") String dir,
				Model model) {
			
			// dir に渡された値が大文字・小文字を区別せず "desc" と一致すれば true！
			Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
			
			// Pageable：ページングの条件（ページ数・件数・並び順など）をまとめて扱うインターフェース
			// PageRequest = Pageable の実装
			Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
			
			
			Page<TeacherDTO> teacherPage = teacherService.getTeacherPage(PageRequest.of(page, size, Sort.by("nickname").ascending()));
			
			System.out.println("teacherPage : " + teacherPage);
			System.out.println("teacherPage totalPages: " + teacherPage.getTotalPages());
			System.out.println("teacherPage content size: " + teacherPage.getContent().size());
			System.out.println("teacherPage isEmpty: " + teacherPage.isEmpty());
			
			model.addAttribute("teacherPage", teacherPage);
			model.addAttribute("currentPage", page);
			model.addAttribute("size", size);
			model.addAttribute("sort", sort);
			model.addAttribute("dir", dir);
			
			return "admin/teacher-list";
			
	}
	
}

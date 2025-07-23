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
import com.english.lms.repository.TeacherListRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TeacherListController {

    private final TeacherListRepository teacherListRepository;

    @GetMapping("/admin/teacher-list")
    public String teacherListPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "nickname") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir,
            @RequestParam(name = "q", required = false) String q,
            Model model) {

        Sort.Direction direction = dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<TeacherDTO> teacherPage;

        if (q != null && !q.isBlank()) {
            teacherPage = teacherListRepository.searchTeachersPage(q, pageable);
        } else {
            teacherPage = teacherListRepository.findAllTeachersPage(pageable);
        }

        model.addAttribute("teacherPage", teacherPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("q", q);

        return "admin/teacher-list";
    }
}

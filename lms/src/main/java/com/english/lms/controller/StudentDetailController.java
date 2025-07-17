package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.english.lms.dto.StudentDetailDTO;
import com.english.lms.repository.StudentDetailRepository;

@Controller
@RequestMapping("/student/detail")
public class StudentDetailController {

    private final StudentDetailRepository studentDetailRepository;

    public StudentDetailController(StudentDetailRepository studentDetailRepository) {
        this.studentDetailRepository = studentDetailRepository;
    }

    @GetMapping("/{studentNum}")
    public String studentDetail(@PathVariable("studentNum") int studentNum, Model model) {
        StudentDetailDTO student = studentDetailRepository.findStudentDetail(studentNum);
        model.addAttribute("student", student);
        return "teacher/StudentDetail";
    }
}

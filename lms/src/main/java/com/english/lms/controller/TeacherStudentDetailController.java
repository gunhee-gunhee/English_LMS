package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.english.lms.dto.StudentDetailDTO;
import com.english.lms.repository.StudentDetailRepository;

@Controller
@RequestMapping("/teacher/student/detail")
public class TeacherStudentDetailController {

    private final StudentDetailRepository studentDetailRepository;

    public TeacherStudentDetailController(StudentDetailRepository studentDetailRepository) {
        this.studentDetailRepository = studentDetailRepository;
    }

    @GetMapping("/{studentNum}")
    public String teacherStudentDetail(@PathVariable("studentNum") int studentNum, Model model) {
        StudentDetailDTO student = studentDetailRepository.findStudentDetail(studentNum);
        model.addAttribute("student", student);
        return "teacher/StudentDetail";
    }
}


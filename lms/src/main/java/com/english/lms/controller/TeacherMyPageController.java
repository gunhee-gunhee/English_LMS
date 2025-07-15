package com.english.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.english.lms.dto.TeacherMyPageDTO;
import com.english.lms.service.TeacherMyPageService;

@RestController
@RequestMapping("/api/teacher-mypage")
public class TeacherMyPageController {

    @Autowired
    private TeacherMyPageService teacherMyPageService;

    @GetMapping("/{teacherNum}/schedule")
    public List<TeacherMyPageDTO> getTeacherSchedule(
            @PathVariable("teacherNum") int teacherNum,
            @RequestParam("date") String date
    ) {
        return teacherMyPageService.getSchedule(teacherNum, date);
    }
    
    @Controller
    public class TeacherMyPageViewController {

        @GetMapping("/teacher/mypage/{teacherNum}")
        public String teacherMypage(@PathVariable("teacherNum") int teacherNum, Model model) {
            model.addAttribute("teacherNum", teacherNum);
            return "teacher/mypage";
        }
    }
}
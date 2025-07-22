package com.english.lms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.english.lms.dto.CustomUserDetails;
import com.english.lms.dto.TeacherMyPageDTO;
import com.english.lms.service.TeacherMyPageService;


@RestController
@RequestMapping("/api/teacher-mypage")
public class TeacherMyPageController {

    @Autowired
    private TeacherMyPageService teacherMyPageService;

    @GetMapping("/schedule")
    public List<TeacherMyPageDTO> getTeacherSchedule(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam("date") String date
    ) {
        String teacherId = user.getUsername(); 
        int teacherNum = teacherMyPageService.getTeacherNumById(teacherId);
        return teacherMyPageService.getSchedule(teacherNum, date);
    }

}

@Controller
class TeacherMyPageViewController { 
    @GetMapping("/teacher/schedule")
    public String teacherMypage(Model model, @AuthenticationPrincipal CustomUserDetails user) {
        model.addAttribute("teacherNum", user.getUsername());
        return "teacher/mypage";
    }
}

package com.english.lms.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.english.lms.dto.AdminDTO;
import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.TeacherScheduleEntity;
import com.english.lms.repository.ClassRepository;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.TeacherScheduleRepository;
import com.english.lms.service.AdminService;
import com.english.lms.service.AdminStudentService;
import com.english.lms.service.TeacherService;
import com.english.lms.service.ZoomAccountService;

/**
 * 学生詳細画面コントローラー（管理者用）
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DetailController {

    private final AdminStudentService adminStudentService;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherScheduleRepository teacherScheduleRepository;
    private final TeacherService teacherService;
    private final ZoomAccountService zoomAccountService;
    private final AdminService adminService;

    /**
     * 学生詳細画面を表示するGET
     */
    @GetMapping("/student/detail/{studentNum}")
    public String showStudentDetail(@PathVariable("studentNum") Integer studentNum, Model model) {
        log.info("学生詳細ページを表示: {}", studentNum);

        // 年齢・英語レベル・講師・曜日・時間などの選択肢
        model.addAttribute("ageList", adminStudentService.getAgeOptions());
        model.addAttribute("englishLevelList", adminStudentService.getEnglishLevelOptions());
        
        Integer teacherNum = classRepository.findTeacherNumByStudentNum(studentNum); // 수업에서 담당 강사 번호 얻기
        String teacherNickname = null;
        if (teacherNum != null) {
            teacherNickname = adminStudentService.getTeacherNickname(teacherNum);
        }
        model.addAttribute("teacherNickname", teacherNickname);
        
        StudentDTO student = adminStudentService.getStudent(studentNum);
        model.addAttribute("student", student);

        return "admin/student-detail";
    }

    /**
     * 学生情報を修正するPOST
     */
    @PostMapping("/student/detail/{studentNum}")
    public String updateStudent(
        @PathVariable("studentNum") Integer studentNum,
        @ModelAttribute("student") StudentDTO dto,
        Model model) {
        adminStudentService.updateStudent(studentNum, dto);
        return "redirect:/admin/student/detail/" + studentNum;
    }

    /**
     * 学生を削除するPOST
     */
    @PostMapping("/student/detail/{studentNum}/delete")
    public String deleteStudent(
            @PathVariable("studentNum") Integer studentNum,
            @ModelAttribute("student") StudentDTO dto,
            Model model) {
        adminStudentService.deleteStudent(studentNum);
        return "redirect:/admin/student-list?deleted";
    }
    

    @GetMapping("/teacher/detail/{teacherNum}")
    public String showTeacherDetail(@PathVariable("teacherNum") Integer teacherNum, Model model) {
        log.info("강사 상세페이지: {}", teacherNum);
        

        TeacherEntity teacher = teacherRepository.findByTeacher(teacherNum)
                .orElseThrow(() -> new RuntimeException("講師無し."));


        List<TeacherScheduleEntity> schedules = teacherScheduleRepository.findAll()
                .stream().filter(s -> s.getTeacherNum().equals(teacherNum))
                .collect(Collectors.toList());


        TeacherDTO teacherDTO = teacherService.toDTO(teacher, schedules);
        
        if (teacherDTO.getSchedules() == null) {
            teacherDTO.setSchedules(new ArrayList<>());
        }


        model.addAttribute("teacherDTO", teacherDTO);
        model.addAttribute("teacherNum", teacherNum);
		List<String> zoomIdList = zoomAccountService.getAllZoomIds();
		model.addAttribute("zoomIdList", zoomIdList);

        return "admin/teacher-detail";
    }
    
    @PostMapping("/teacher/detail/{teacherNum}")
    public String updateTeacher(
            @PathVariable("teacherNum") Integer teacherNum,
            @ModelAttribute("teacherDTO") TeacherDTO teacherDTO,
            Model model) {
        teacherService.updateTeacher(teacherNum, teacherDTO);
        return "redirect:/admin/teacher/detail/" + teacherNum + "?success";
    }
    
    @GetMapping("/admin/detail/{adminNum}")
    public String showAdminDetail(@PathVariable("adminNum") Integer adminNum, Model model) {
        AdminDTO admin = adminService.getAdmin(adminNum);
        model.addAttribute("adminDTO", admin);
        return "admin/admin-detail";
    }

    @PostMapping("/admin/detail/{adminNum}")
    public String updateAdmin(
            @PathVariable("adminNum") Integer adminNum,
            @ModelAttribute("adminDTO") AdminDTO dto,
            Model model) {
        adminService.updateAdmin(adminNum, dto);
        return "redirect:/admin/detail/" + adminNum + "?success";
    }
    
    @PostMapping("/admin/detail/{adminNum}/delete")
    public String deleteAdmin(@PathVariable("adminNum") Integer adminNum) {
        adminService.deleteAdmin(adminNum);
        return "redirect:/admin-list?deleted";
    }
}

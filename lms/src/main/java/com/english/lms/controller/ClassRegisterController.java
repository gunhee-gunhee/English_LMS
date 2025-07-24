package com.english.lms.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.english.lms.dto.ClassDTO;
import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.entity.TextEntity;
import com.english.lms.service.AdminStudentService;
import com.english.lms.service.TeacherService;
import com.english.lms.service.TextService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClassRegisterController {
	
	private final AdminStudentService adminStudentService;
	private final TeacherService teacherService;
	private final TextService textService;

	//Page移動
	@GetMapping("/admin/class/register")
	public String showPage(
			//リストのサイズ
			@RequestParam(name = "size", defaultValue = "10") int size,
			
			//学生リスト
			@RequestParam(name = "studentSort", defaultValue = "studentId" ) String studentSort,
			@RequestParam(name = "studentDir", defaultValue = "asc") String studentDir,
			
			//講師リスト
			@RequestParam(name = "teacherSort", defaultValue = "teacherId") String teacherSort,
			@RequestParam(name = "teacherDir", defaultValue = "asc") String teacherDir,
			Model model) {
			
		// ソート条件と並び順は Pageable で構成
		// dir に渡された値が大文字・小文字を区別せず "desc" と一致すれば true！
		Sort.Direction studentDirection = studentDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		
//		Pageable studentPageable = PageRequest.of(0, size, Sort.by(studentDirection, studentSort));
		
//		Page<StudentDTO> studentList = adminStudentService.getStudentPageWithTeacher(studentPageable);
		
		List<StudentDTO> studentList = adminStudentService.getActiveStudents(studentSort, studentDir);
		
		Page<TeacherDTO> teacherList = teacherService.getTeacherPage(PageRequest.of(0, size, Sort.by("nickname").ascending()));
		
		List<TextEntity> texts = textService.findAll();
		System.out.println(texts.get(0).getTextNum());
		model.addAttribute("studentList", studentList);
		model.addAttribute("teacherList", teacherList);
		model.addAttribute("studentSort", studentSort);
		model.addAttribute("studentDir", studentDir);
		model.addAttribute("teacherSort", teacherSort);
		model.addAttribute("teacherDir", teacherDir);
		model.addAttribute("classDTO", new ClassDTO());
		model.addAttribute("texts", texts);
		
		return "admin/class-register";
	}
	
	
	/** 
	 * 定期授業を登録するメソッド
	 * @param　classDTO
	 * @return 
	 * */
	
	@PostMapping("/admin/regular/register")
	public String registerRegularClass(@ModelAttribute("classDTO") ClassDTO classDTO) {
		
		System.out.println("studentNum " + classDTO.getStudentNum());
		System.out.println("weekDays " +classDTO.getWeekDays());
		System.out.println("startHour " + classDTO.getStartHour());
		System.out.println("startMinute "+ classDTO.getStartMinute());
		System.out.println("endHour " + classDTO.getEndHour());
		System.out.println("endMinute " +classDTO.getEndMinute());
		System.out.println("textNum " +classDTO.getTextNum());
		System.out.println("teacherNum " +classDTO.getTeacherNum());
		
		return "redirect:/admin/class/register";
	}
	

	
	
}

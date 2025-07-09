package com.english.lms.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.english.lms.dto.AdminDTO;
import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.service.AdminService;
import com.english.lms.service.StudentService;
import com.english.lms.service.ZoomAccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminRegisterController {
	
	private final ZoomAccountService zoomAccountService;
	private final StudentService studentService;
	private final AdminService adminService;
	
	//Page移動
	@GetMapping("/admin/register")
	public String showPage(Model model) {
		
		//講師フォームの授業開始日設定
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setStartDate(LocalDate.now()); // 今日に設定する
		
		model.addAttribute("studentDTO", new StudentDTO());
		model.addAttribute("teacherDTO",  teacherDTO);
		model.addAttribute("adminDTO", new AdminDTO());	
		
		List<String> zoomIdList = zoomAccountService.getAllZoomIds();
		model.addAttribute("zoomIdList", zoomIdList);
		
		return "admin/register";
	}
	
	//student　会員登録
	@PostMapping("/admin/student/register")
	public String studentRegister(@ModelAttribute StudentDTO studentDTO, RedirectAttributes redirectAttributes ) {
		
		//パスワードのチェック
		  if (!studentDTO.getPassword().equals(studentDTO.getPasswordCheck())) {
	            redirectAttributes.addFlashAttribute("studentError", "パスワードが一致しません。");
	            redirectAttributes.addFlashAttribute("studentDTO", studentDTO);
	            redirectAttributes.addFlashAttribute("openForm", "student");
	            return "redirect:/admin/register?open=student";
	        }
		 
		//重複のIDをチェック
		  if (studentService.existsById(studentDTO.getId())) {
	            redirectAttributes.addFlashAttribute("studentError", "このIDは既に登録されています。");
	            redirectAttributes.addFlashAttribute("studentDTO", studentDTO);
	            redirectAttributes.addFlashAttribute("openForm", "student");
	            return "redirect:/admin/register?open=student";
	        }
		  
		//登録
		 studentService.registerStudent(studentDTO);
		
		//メッセージ
		 redirectAttributes.addFlashAttribute("studentSuccess", "会員登録が完了しました。");
		 redirectAttributes.addFlashAttribute("openForm", "student");
		return  "redirect:/admin/register?open=student";
	}
	
	//center 会員登録
	@PostMapping("/admin/center/register")
	public String centerRegister(@ModelAttribute AdminDTO adminDTO, RedirectAttributes redirectAttributes) {
		
		//重複のIDをチェック
		if(adminService.existsById(adminDTO.getId())) {
			redirectAttributes.addFlashAttribute("centerError", "このIDは既に登録されています。" );
			redirectAttributes.addFlashAttribute("adminDTO", adminDTO);
			redirectAttributes.addFlashAttribute("openForm", "center");
			return "redirect:/admin/register?open=center";
		}
		
		//パスワードのチェック
		if(!adminDTO.getPassword().equals(adminDTO.getPasswordCheck())) {
			redirectAttributes.addFlashAttribute("centerError", "パスワードが一致しません。" );
			redirectAttributes.addFlashAttribute("adminDTO", adminDTO);
			redirectAttributes.addFlashAttribute("openForm", "center");
			return "redirect:/admin/register?open=center";
		}
		

		//登録
		adminService.registerCenter(adminDTO);
		
		//メッセージ
		redirectAttributes.addFlashAttribute("centerSuccess", "会員登録が完了しました。");
		redirectAttributes.addFlashAttribute("openForm","center");
		return "redirect:/admin/register?open=center";
		
	}
	
	//admin会員登録
	@PostMapping("/admin/register")
	public String adminRegister(@ModelAttribute AdminDTO adminDTO, RedirectAttributes redirectAttributes) {
		
		//重複のIDをチェック
		if(adminService.existsById(adminDTO.getId())) {
			redirectAttributes.addFlashAttribute("adminError", "このIDは既に登録されています。" );
			redirectAttributes.addFlashAttribute("adminDTO", adminDTO);
			redirectAttributes.addFlashAttribute("openForm", "admin");
			return "redirect:/admin/register?open=admin";
		}		
				
		//パスワードのチェック
		if(!adminDTO.getPassword().equals(adminDTO.getPasswordCheck())) {
			redirectAttributes.addFlashAttribute("adminError", "パスワードが一致しません。" );
			redirectAttributes.addFlashAttribute("adminDTO", adminDTO);
			redirectAttributes.addFlashAttribute("openForm", "admin");
			return "redirect:/admin/register?open=admin";
		}		
				

		//登録
		adminService.registerAdmin(adminDTO);
		
		//メッセージ
		redirectAttributes.addFlashAttribute("adminSuccess", "会員登録が完了しました。");
		redirectAttributes.addFlashAttribute("openForm","admin");
		return "redirect:/admin/register?open=admin";		
		
		}
	
	//teacher会員登録
	@PostMapping("/admin/teacher/register")
	public String teacherRegister(@ModelAttribute TeacherDTO teacherDTO, RedirectAttributes redirectAttributes ) {
		
		// 時間（時と分）を結合してLocalTimeを作成
		//LocalTime.of（hour, minute) 
		LocalTime startTime = LocalTime.of(teacherDTO.getStartHour(),teacherDTO.getStartMinute());
		LocalTime endTime = LocalTime.of(teacherDTO.getEndHour(), teacherDTO.getEndMinute());
		
		// 曜日ごとにタイムスロットを保存
		for(String weekday : teacherDTO.getWeekdays()) {
			
		}
		return "";
	}
}

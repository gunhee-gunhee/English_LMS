package com.english.lms.controller;

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
import com.english.lms.service.StudentService;
import com.english.lms.service.ZoomAccountService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminRegisterController {
	
	private final ZoomAccountService zoomAccountService;
	private final StudentService studentService;
	
	//Page移動
	@GetMapping("/admin/register")
	public String showPage(Model model) {
		model.addAttribute("studentDTO", new StudentDTO());
		model.addAttribute("teacherDTO", new TeacherDTO());
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
		 redirectAttributes.addFlashAttribute("success", "会員登録が完了しました。");
		 redirectAttributes.addFlashAttribute("openForm", "student");
		return  "redirect:/admin/register?open=student";
	}
	
	
}

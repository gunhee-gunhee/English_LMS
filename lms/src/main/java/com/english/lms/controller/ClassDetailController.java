package com.english.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.english.lms.dto.ClassDetailDTO;
import com.english.lms.repository.ClassDetailRepository;

@Controller
@RequestMapping("/teacher/day-class")
public class ClassDetailController {

    @Autowired
    private ClassDetailRepository classDetailRepository;

    // GET: 상세 보기
    @GetMapping("/{dayClassNum}")
    public String detailPage(@PathVariable("dayClassNum") int dayClassNum, Model model) {
        ClassDetailDTO dto = classDetailRepository.getClassDetail(dayClassNum);
        model.addAttribute("detail", dto);
        return "teacher/Classdetail";
    }

    @PostMapping("/{dayClassNum}")
    public String update(
    		@PathVariable("dayClassNum") int dayClassNum,  
    	    @RequestParam("comment") String comment,
    	    @RequestParam("progress") String progress,
                         RedirectAttributes redirectAttributes) {
        classDetailRepository.updateCommentProgress(dayClassNum, comment, progress);
        redirectAttributes.addFlashAttribute("msg", "Saved successfully!");
        return "redirect:/teacher/day-class/" + dayClassNum;
    }
}

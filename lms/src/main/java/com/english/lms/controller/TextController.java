package com.english.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.english.lms.service.TextService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/text")
@RequiredArgsConstructor
public class TextController {
    private final TextService textService;

    @GetMapping("/master")
    public String showTextMaster(Model model) {
        model.addAttribute("texts", textService.findAll());
        return "admin/text"; 
    }

    @PostMapping("/add")
    public String addText(@RequestParam("name") String name) {
        textService.addText(name);
        return "redirect:/admin/text/master";
    }

    @PostMapping("/delete/{textNum}")
    public String deleteText(@PathVariable("textNum") Integer textNum) {
        textService.deleteText(textNum);
        return "redirect:/admin/text/master";
    }
}

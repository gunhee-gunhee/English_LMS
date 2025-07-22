package com.english.lms.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.english.lms.service.SystemSettingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/system")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingService systemSettingService;

    @GetMapping
    public String showSettings(Model model) {
        Map<String, String> settings = systemSettingService.getAllSettings();
        model.addAttribute("settings", settings);
        return "admin/system-setting";
    }

    @PostMapping
    public String updateSettings(@RequestParam Map<String, String> params, RedirectAttributes redirectAttributes) {
        // params: key-value map from form
        systemSettingService.updateSettings(params);
        redirectAttributes.addFlashAttribute("msg", "設定を保存しました。");
        return "redirect:/admin/system";
    }
}

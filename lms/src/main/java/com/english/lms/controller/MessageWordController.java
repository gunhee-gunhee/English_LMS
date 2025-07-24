package com.english.lms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.english.lms.service.MessageWordService;

@Controller
@RequestMapping("/admin/message")
public class MessageWordController {

    private final MessageWordService service;

    public MessageWordController(MessageWordService service) {
        this.service = service;
    }

    // メッセージ一覧＋登録フォーム
    @GetMapping
    public String list(Model model) {
        model.addAttribute("messages", service.findAll());
        return "admin/message-setting"; // テンプレート名
    }

    // 登録処理
    @PostMapping("/add")
    public String add(@RequestParam("word") String word,
                      @RequestParam("word_jp") String wordJp) {
        service.register(word, wordJp);
        return "redirect:/admin/message";
    }

    // 削除処理
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        service.delete(id);
        return "redirect:/admin/message";
    }

    // （お知らせ作成用）ドロップダウンのみ表示ページ
    @GetMapping("/select")
    public String selectQuick(Model model) {
        model.addAttribute("messages", service.findAll());
        return "admin/emergency-message";
    }
}

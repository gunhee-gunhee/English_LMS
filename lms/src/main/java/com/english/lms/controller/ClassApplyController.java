package com.english.lms.controller;

import com.english.lms.dto.ClassApplyDto;
import com.english.lms.service.ClassApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("student")
public class ClassApplyController {

    private final ClassApplyService ClassApplyService;


    @GetMapping("/apply")
    public String showApplyForm() {
        return "student/class-apply";
    }


    @PostMapping("/apply")
    public String applyTrial(
    		@AuthenticationPrincipal(expression = "username") String studentId,
            @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("course") String course,
            @RequestParam(value = "time1", required = false) String firstChoice,
            @RequestParam(value = "time2", required = false) String secondChoice,
            @RequestParam(value = "time3", required = false) String thirdChoice
    ) {
        ClassApplyDto dto = ClassApplyDto.builder()
                .studentId(studentId)
                .startDate(startDate)
                .course(course)
                .firstChoice(firstChoice)
                .secondChoice(secondChoice)
                .thirdChoice(thirdChoice)
                .build();

        log.info("申込情報: {}", dto);
        ClassApplyService.apply(dto);
        return "redirect:/student/thanks";
    }

    @GetMapping("/thanks")
    public String showThanksPage() {
        return "student/thanks";
    }
}

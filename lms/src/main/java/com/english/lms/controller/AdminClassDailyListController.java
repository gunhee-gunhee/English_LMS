package com.english.lms.controller;

import com.english.lms.dto.DayClassDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminClassDailyListController {

    private final DayClassRepository dayClassRepository;
    private final TeacherRepository teacherRepository;

    // --- 공통 리스트 핸들러 ---
    private String handleDayClassDailyListPage(
            String type,
            LocalDate startDate, LocalDate endDate, int page, int size,
            String sort, String dir, String q,
            Model model
    ) {
        LocalDate today = LocalDate.now();
        String todayString = today.toString();

        LocalDate from = (startDate != null) ? startDate : today;
        LocalDate to = (endDate != null) ? endDate : today;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sort));
        Page<DayClassDTO> classPage;
        if (q != null && !q.trim().isEmpty()) {
            classPage = dayClassRepository.searchDayClassesWithDetailsByRange(from, to, q, type, pageable);
        } else {
            classPage = dayClassRepository.findAllDayClassesWithDetailsByRange(from, to, type, pageable);
        }

        model.addAttribute("classPage", classPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("todayString", todayString);
        model.addAttribute("q", q);

        return "regular".equals(type) ? "admin/regular-class-daily-list" : "admin/irregular-class-daily-list";
    }

    // 정규수업 일별 리스트
    @GetMapping("/admin/regular-class-daily-list")
    public String regularClassDailyListPage(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "startTime") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir,
            @RequestParam(name = "q", required = false) String q,
            Model model
    ) {
        return handleDayClassDailyListPage("regular", startDate, endDate, page, size, sort, dir, q, model);
    }

    // 비정규수업 일별 리스트
    @GetMapping("/admin/irregular-class-daily-list")
    public String irregularClassDailyListPage(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "startTime") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir,
            @RequestParam(name = "q", required = false) String q,
            Model model
    ) {
        return handleDayClassDailyListPage("irregular", startDate, endDate, page, size, sort, dir, q, model);
    }

    // --- 공통 상세 핸들러 ---
    private String handleDayClassDailyDetailPage(
            String type,
            Integer dayClassNum,
            Model model
    ) {
        DayClassDTO classDetail = dayClassRepository.findDayClassDetailByDayClassNum(dayClassNum, type);
        List<TeacherEntity> teacherList = teacherRepository.findAll();
        model.addAttribute("classDetail", classDetail);
        model.addAttribute("teacherList", teacherList);

        return "regular".equals(type) ? "admin/regular-class-daily-detail" : "admin/irregular-class-daily-detail";
    }

    // 정규수업 상세(수정폼 포함)
    @GetMapping("/admin/regular-class-daily-detail/{dayClassNum}")
    public String regularClassDailyDetailPage(
            @PathVariable("dayClassNum") Integer dayClassNum,
            Model model
    ) {
        return handleDayClassDailyDetailPage("regular", dayClassNum, model);
    }

    // 비정규수업 상세(수정폼 포함)
    @GetMapping("/admin/irregular-class-daily-detail/{dayClassNum}")
    public String irregularClassDailyDetailPage(
            @PathVariable("dayClassNum") Integer dayClassNum,
            Model model
    ) {
        return handleDayClassDailyDetailPage("irregular", dayClassNum, model);
    }

    // --- 공통 수정 핸들러 ---
    private String handleDayClassDailyEdit(String type, DayClassDTO form) {
        dayClassRepository.updateDayClassByDayClassNum(form, type);
        return "redirect:/admin/" + ("regular".equals(type) ? "regular" : "irregular") + "-class-daily-detail/" + form.getDayClassNum();
    }

    // 정규수업 인라인 수정 저장
    @PostMapping("/admin/regular-class-daily-edit")
    public String regularClassDailyEdit(@ModelAttribute DayClassDTO form, Model model) {
        return handleDayClassDailyEdit("regular", form);
    }

    // 비정규수업 인라인 수정 저장
    @PostMapping("/admin/irregular-class-daily-edit")
    public String irregularClassDailyEdit(@ModelAttribute DayClassDTO form, Model model) {
        return handleDayClassDailyEdit("irregular", form);
    }

    // --- 공통 삭제 핸들러 ---
    private String handleDayClassDailyDelete(String type, Integer dayClassNum) {
        dayClassRepository.deleteDayClassByDayClassNum(dayClassNum, type);
        return "redirect:/admin/" + ("regular".equals(type) ? "regular" : "irregular") + "-class-daily-list";
    }

    // 정규수업 삭제
    @PostMapping("/admin/day-class-delete")
    public String deleteRegularDayClass(@RequestParam("dayClassNum") Integer dayClassNum) {
        return handleDayClassDailyDelete("regular", dayClassNum);
    }

    // 비정규수업 삭제
    @PostMapping("/admin/irregular-day-class-delete")
    public String deleteIrregularDayClass(@RequestParam("dayClassNum") Integer dayClassNum) {
        return handleDayClassDailyDelete("irregular", dayClassNum);
    }
}

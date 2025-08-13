package com.english.lms.controller;

import com.english.lms.dto.ClassApplyDto; 
import com.english.lms.service.AdminClassApplyListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class AdminClassApplyListController {

    private final AdminClassApplyListService applyListService;

    /**
     * 정기 수업 신청 리스트 페이지
     */
    @GetMapping("/admin/regular-class-apply-list")
    public String list(
            @RequestParam(value = "year",  required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,  
            @RequestParam(value = "q",     required = false) String q,
            @RequestParam(value = "page",  required = false, defaultValue = "0")  int page,
            @RequestParam(value = "size",  required = false, defaultValue = "25") int size,
            @RequestParam(value = "sort",  required = false) String sort,
            @RequestParam(value = "dir",   required = false) String dir,
            Model model
    ) {
        LocalDate now = LocalDate.now();
        int selectedYear  = (year  == null) ? now.getYear() : year;
        int selectedMonth = (month == null || month < 1 || month > 12) ? now.getMonthValue() : month;

        String keyword = (q == null) ? "" : q.trim();
        String sortKey = sanitizeSortKey(sort);
        String sortDir = (dir == null || dir.isBlank()) ? "desc" : dir.trim().toLowerCase(Locale.ROOT);

        Sort.Direction direction = "asc".equals(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortKey));

        Page<ClassApplyDto> applyPage =
                applyListService.findApplyPage(selectedYear, selectedMonth, keyword, pageable);

        List<Integer> yearList = IntStream.rangeClosed(now.getYear() - 1, now.getYear() + 1)
                .boxed().collect(Collectors.toList());

        model.addAttribute("applyPage", applyPage);
        model.addAttribute("yearList", yearList);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("q", keyword);
        model.addAttribute("sort", sortKey);
        model.addAttribute("dir", sortDir);
        model.addAttribute("size", size);
        model.addAttribute("currentPage", page);

        return "admin/regular-class-apply-list";
    }

    /**
     * 정기 수업 신청 상세 페이지
     */
    @GetMapping("/admin/regular-class-apply-detail/{applyNum}")
    public String applyDetail(
            @PathVariable("applyNum") Long applyNum, // ← 이름 명시!
            Model model
    ) {
        ClassApplyDto applyDetail = applyListService.findById(applyNum);
        if (applyDetail == null) {
            return "redirect:/admin/regular-class-apply-list";
        }
        model.addAttribute("applyDetail", applyDetail);
        return "admin/regular-class-apply-detail";
    }

    /**
     * 허용된 정렬 키만 통과
     */
    private String sanitizeSortKey(String key) {
        if (key == null || key.isBlank()) return "applyDate";
        switch (key) {
            case "studentNickname":
            case "studentId":
            case "startDate":
            case "course":
            case "firstChoice":
            case "secondChoice":
            case "thirdChoice":
            case "applyDate":
                return key;
            default:
                return "applyDate";
        }
    }
}

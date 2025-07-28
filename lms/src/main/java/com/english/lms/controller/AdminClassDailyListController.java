package com.english.lms.controller;

import com.english.lms.dto.DayClassDTO;
import com.english.lms.repository.DayClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AdminClassDailyListController {

    private final DayClassRepository dayClassRepository;

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
        LocalDate today = LocalDate.now();
        String todayString = today.toString();

        LocalDate from = (startDate != null) ? startDate : today;
        LocalDate to = (endDate != null) ? endDate : today;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sort));
        Page<DayClassDTO> classPage;
        if (q != null && !q.trim().isEmpty()) {
            classPage = dayClassRepository.searchRegularDayClassesWithDetailsByRange(from, to, q, pageable);
        } else {
            classPage = dayClassRepository.findAllRegularDayClassesWithDetailsByRange(from, to, pageable);
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

        return "admin/regular-class-daily-list";
    }

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
        LocalDate today = LocalDate.now();
        String todayString = today.toString();

        LocalDate from = (startDate != null) ? startDate : today;
        LocalDate to = (endDate != null) ? endDate : today;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sort));
        Page<DayClassDTO> classPage;
        if (q != null && !q.trim().isEmpty()) {
            classPage = dayClassRepository.searchIrregularDayClassesWithDetailsByRange(from, to, q, pageable);
        } else {
            classPage = dayClassRepository.findAllIrregularDayClassesWithDetailsByRange(from, to, pageable);
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

        return "admin/irregular-class-daily-list";
    }
}

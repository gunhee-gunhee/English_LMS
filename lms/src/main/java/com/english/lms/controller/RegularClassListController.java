package com.english.lms.controller;

import com.english.lms.dto.ClassDTO;
import com.english.lms.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegularClassListController {

    private final ClassRepository classRepository;

    @GetMapping("/admin/regular-class-list")
    public String regularClassListPage(
        @RequestParam(name = "year", required = false) String year,
        @RequestParam(name = "month", required = false) Integer month,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "sort", defaultValue = "classNum") String sort,
        @RequestParam(name = "dir", defaultValue = "asc") String dir,
        @RequestParam(name = "q", required = false) String q,
        Model model
    ) {
        // 연도 옵션(존재하는 연도만)
        List<String> yearList = classRepository.findDistinctYearsFromClassMonth();

        LocalDate now = LocalDate.now();
        String defaultYear = String.valueOf(now.getYear());
        int defaultMonth = now.getMonthValue();

        // ★ 최신 연도를 디폴트로 설정 (yearList가 비어있지 않으면 마지막 값)
        String selectedYear;
        if (year != null) {
            selectedYear = year;
        } else if (!yearList.isEmpty()) {
            selectedYear = yearList.get(yearList.size() - 1); // 최신 연도
        } else {
            selectedYear = defaultYear;
        }

        Integer selectedMonth = (month != null) ? month : defaultMonth;

        // 화면에 전달
        model.addAttribute("yearList", yearList);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sort));
        Page<ClassDTO> classPage;

        // ★ 파라미터 조합으로 classMonth("2025-07" 등)
        String classMonth = (selectedYear != null && selectedMonth != null)
                ? String.format("%s-%02d", selectedYear, selectedMonth) : null;

        // 필터링: 검색어 + 월/년
        if (q != null && !q.trim().isEmpty()) {
            classPage = classRepository.searchClassesWithDetailsAndMonth(q, classMonth, pageable);
        } else {
            classPage = classRepository.findAllClassesWithDetailsAndMonth(classMonth, pageable);
        }

        model.addAttribute("classPage", classPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("q", q);

        return "admin/regular-class-list";
    }
}

package com.english.lms.controller;

import com.english.lms.dto.ClassDTO;
import com.english.lms.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminClassListController {

    private final ClassRepository classRepository;

    // 리스트
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

        // 최신 연도를 디폴트로 설정
        String selectedYear;
        if (year != null) {
            selectedYear = year;
        } else if (!yearList.isEmpty()) {
            selectedYear = yearList.get(yearList.size() - 1); // 최신 연도
        } else {
            selectedYear = defaultYear;
        }
        Integer selectedMonth = (month != null) ? month : defaultMonth;

        model.addAttribute("yearList", yearList);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("selectedMonth", selectedMonth);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sort));
        Page<ClassDTO> classPage;

        String classMonth = (selectedYear != null && selectedMonth != null)
                ? String.format("%s-%02d", selectedYear, selectedMonth) : null;

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

    // 상세
    @GetMapping("/admin/regular-class-detail/{classNum}")
    public String classDetailPage(
            @PathVariable("classNum") Integer classNum,  // ← 이름 명시!
            Model model
    ) {
        // 상세 데이터 조회 (repository에 해당 메서드 추가)
        ClassDTO classDetail = classRepository.findClassDetailByClassNum(classNum);
        model.addAttribute("classDetail", classDetail);
        return "admin/regular-class-detail";
    }

    // 삭제
    @PostMapping("/admin/regular-class-detail/delete")
    public String deleteClass(@RequestParam("classNum") Integer classNum) { // ← 이름 명시!
        classRepository.deleteById(classNum); // JPA CrudRepository 상속시 자동지원
        return "redirect:/admin/regular-class-list";
    }
}

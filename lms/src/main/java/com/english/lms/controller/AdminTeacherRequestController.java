package com.english.lms.controller;

import com.english.lms.dto.TeacherRequestDTO;
import com.english.lms.repository.TeacherRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AdminTeacherRequestController {

    private final TeacherRequestRepository teacherRequestRepository;

    // --- 리스트 공통 핸들러 ---
    private String handleTeacherRequestListPage(
            LocalDate startDate, LocalDate endDate,
            int page, int size, String sort, String dir, String q,
            Model model
    ) {
        LocalDate today = LocalDate.now();
        String todayString = today.toString();

        LocalDate from = (startDate != null) ? startDate : today;
        LocalDate to   = (endDate   != null) ? endDate   : today;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(dir), sort));

        Page<TeacherRequestDTO> requestPage;
        if (q != null && !q.trim().isEmpty()) {
            // 검색어가 있는 경우
            requestPage = teacherRequestRepository.searchTeacherRequestsByRange(from, to, q, pageable);
        } else {
            // 전체 목록
            requestPage = teacherRequestRepository.findTeacherRequestsByRange(from, to, pageable);
        }

        model.addAttribute("requestPage", requestPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("todayString", todayString);
        model.addAttribute("q", q);

        return "admin/teacher-request-list";
    }

    // 강사 리퀘스트 목록
    @GetMapping("/admin/teacher-request-list")
    public String teacherRequestListPage(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate",   required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "requestDate") String sort,
            @RequestParam(name = "dir",  defaultValue = "desc") String dir,
            @RequestParam(name = "q", required = false) String q,
            Model model
    ) {
        return handleTeacherRequestListPage(startDate, endDate, page, size, sort, dir, q, model);
    }

    // --- 상세 페이지 ---
    @GetMapping("/admin/teacher-request-detail/{requestNum}")
    public String teacherRequestDetailPage(
            @PathVariable("requestNum") Integer requestNum,
            Model model
    ) {
        // 단건 조회 (DTO로 투영 가정)
        TeacherRequestDTO detail = teacherRequestRepository.findDetailByRequestNum(requestNum);
        model.addAttribute("detail", detail);
        return "admin/teacher-request-detail";
    }
}

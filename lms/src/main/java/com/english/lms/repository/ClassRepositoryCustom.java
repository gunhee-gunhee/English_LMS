package com.english.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.english.lms.dto.ClassDTO;

public interface ClassRepositoryCustom {
    Page<ClassDTO> findAllClassesWithDetails(Pageable pageable);
    Page<ClassDTO> searchClassesWithDetails(String q, Pageable pageable);

    // ★ 추가
    Page<ClassDTO> findAllClassesWithDetailsAndMonth(String classMonth, Pageable pageable);
    Page<ClassDTO> searchClassesWithDetailsAndMonth(String q, String classMonth, Pageable pageable);
}
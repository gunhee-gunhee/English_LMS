package com.english.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.english.lms.dto.ClassDTO;
import java.util.List;

public interface ClassRepositoryCustom {
    Page<ClassDTO> findAllClassesWithDetails(Pageable pageable);
    Page<ClassDTO> searchClassesWithDetails(String q, Pageable pageable);
    Page<ClassDTO> findAllClassesWithDetailsAndMonth(String classMonth, Pageable pageable);
    Page<ClassDTO> searchClassesWithDetailsAndMonth(String q, String classMonth, Pageable pageable);

    // 상세조회
    ClassDTO findClassDetailByClassNum(Integer classNum);

    // ★ 년도 목록 (class_month에서 년도만 추출, 중복 없이)
    List<String> findDistinctYearsFromClassMonth();
}

package com.english.lms.repository;

import com.english.lms.dto.DayClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface DayClassRepositoryCustom {
    // 단일 날짜 (호환)
    Page<DayClassDTO> findAllRegularDayClassesWithDetailsByDaily(LocalDate classDate, Pageable pageable);
    Page<DayClassDTO> searchRegularDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, Pageable pageable);

    // 날짜 범위(정규)
    Page<DayClassDTO> findAllRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable);
    Page<DayClassDTO> searchRegularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable);

    // 날짜 범위(비정규)
    Page<DayClassDTO> findAllIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, Pageable pageable);
    Page<DayClassDTO> searchIrregularDayClassesWithDetailsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable);
}

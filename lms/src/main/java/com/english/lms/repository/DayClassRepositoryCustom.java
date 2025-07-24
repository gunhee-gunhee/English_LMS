package com.english.lms.repository;

import com.english.lms.dto.DayClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface DayClassRepositoryCustom {
    Page<DayClassDTO> findAllRegularDayClassesWithDetailsByDaily(LocalDate classDate, Pageable pageable);
    Page<DayClassDTO> searchRegularDayClassesWithDetailsByDaily(LocalDate classDate, String keyword, Pageable pageable);
}

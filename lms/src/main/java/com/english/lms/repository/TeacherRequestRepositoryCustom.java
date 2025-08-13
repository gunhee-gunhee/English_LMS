package com.english.lms.repository;

import com.english.lms.dto.TeacherRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TeacherRequestRepositoryCustom {

    Page<TeacherRequestDTO> findTeacherRequestsByRange(LocalDate from, LocalDate to, Pageable pageable);

    Page<TeacherRequestDTO> searchTeacherRequestsByRange(LocalDate from, LocalDate to, String keyword, Pageable pageable);

    TeacherRequestDTO findDetailByRequestNum(Integer requestNum);
}

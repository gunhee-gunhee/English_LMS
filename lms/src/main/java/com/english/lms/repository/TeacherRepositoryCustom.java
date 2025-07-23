package com.english.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.english.lms.dto.TeacherDTO;

public interface TeacherRepositoryCustom {
    Page<TeacherDTO> findAllTeachersPage(Pageable pageable);
    Page<TeacherDTO> searchTeachersPage(String keyword, Pageable pageable);
}

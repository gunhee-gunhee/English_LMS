package com.english.lms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.english.lms.dto.StudentDTO;

//// ここにカスタムメソッドを定義
public interface StudentRepositoryCustom {

	Page<StudentDTO> searchStudentsPageWithTeacher(String keyword, Pageable pageable);
    Page<StudentDTO> findAllStudentsPageWithTeacher(Pageable pageable);
}

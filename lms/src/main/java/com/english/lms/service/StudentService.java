package com.english.lms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.english.lms.dto.StudentDTO;

public interface StudentService {
    void registerStudent(StudentDTO dto);
    boolean existsById(String id);
//	List<StudentDTO> getAllStudentWithTeacher();
	Page<StudentDTO> getStudentPageWithTeacher(Pageable pageable);
}
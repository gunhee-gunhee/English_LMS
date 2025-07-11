package com.english.lms.service;

import com.english.lms.dto.StudentDTO;


public interface StudentService {
    void registerStudent(StudentDTO dto);
    boolean existsById(String id);
}
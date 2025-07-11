package com.english.lms.service;

import com.english.lms.dto.TeacherDTO;

public interface TeacherService {

	boolean existsById(String id);

	void registerTeacher(TeacherDTO teacherDTO);

}

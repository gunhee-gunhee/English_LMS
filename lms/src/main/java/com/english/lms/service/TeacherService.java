package com.english.lms.service;

import java.util.List;

import com.english.lms.dto.TeacherDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.TeacherScheduleEntity;

public interface TeacherService {

	boolean existsById(String id);

	void registerTeacher(TeacherDTO teacherDTO);
	
    TeacherDTO toDTO(TeacherEntity entity, List<TeacherScheduleEntity> schedules);
    void updateTeacher(Integer teacherNum, TeacherDTO teacherDTO);
    void deleteTeacher(Integer teacherNum);

}

package com.english.lms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.TeacherScheduleEntity;

public interface TeacherService {

	boolean existsById(String id);

	void registerTeacher(TeacherDTO teacherDTO);
	
    TeacherDTO toDTO(TeacherEntity entity, List<TeacherScheduleEntity> schedules);
    void updateTeacher(Integer teacherNum, TeacherDTO teacherDTO);
    
    // 教師照会ページで使用される処理メソッド
	Page<TeacherDTO> getTeacherPage(Pageable pageable);

}

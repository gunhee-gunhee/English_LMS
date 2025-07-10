package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.english.lms.entity.TeacherEntity;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {

	Optional<TeacherEntity> findByTeacherId(String teacherId);
	
	Optional<TeacherEntity> findByTeacherNum(Integer teacherNum);
}

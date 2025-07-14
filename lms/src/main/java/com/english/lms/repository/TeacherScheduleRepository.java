package com.english.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.english.lms.entity.TeacherScheduleEntity;

public interface TeacherScheduleRepository extends JpaRepository<TeacherScheduleEntity, Integer> {
	
	@Modifying
	@Query("DELETE FROM TeacherScheduleEntity t WHERE t.teacherNum = :teacherNum")
	void deleteByTeacherNum(@Param("teacherNum") Integer teacherNum);

}

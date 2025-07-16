package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.english.lms.entity.TeacherEntity;

public interface TeacherRepository extends JpaRepository<TeacherEntity, Integer> {

	Optional<TeacherEntity> findByTeacherId(String teacherId);

	//学生リストで講師のニックネームを取得するクエリ
	@Query(
			value = "SELECT * FROM lms_teacher WHERE teacher_num = :teacherNum",
			nativeQuery = true
			)
	Optional<TeacherEntity> findByTeacher(@Param("teacherNum") Integer teacherNum);
}

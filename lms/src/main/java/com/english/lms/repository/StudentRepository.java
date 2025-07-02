package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {

	//JPA 커스텀 메서드 작성 
	Optional<StudentEntity> findByStudentId(String studentId); 
}

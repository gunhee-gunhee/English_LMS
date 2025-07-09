package com.english.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.english.lms.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
 
	Optional<StudentEntity> findByStudentId(String studentId); 
	boolean existsByStudentId(String studentId);
	
	// age 중복 없이 모두 추출
	@Query("SELECT DISTINCT s.age FROM StudentEntity s WHERE s.age IS NOT NULL ORDER BY s.age")
	List<String> findDistinctAges();

	// english_level 중복 없이 모두 추출
	@Query("SELECT DISTINCT s.englishLevel FROM StudentEntity s WHERE s.englishLevel IS NOT NULL ORDER BY s.englishLevel")
	List<Integer> findDistinctEnglishLevels();

	// company 중복 없이 모두 추출
	@Query("SELECT DISTINCT s.company FROM StudentEntity s WHERE s.company IS NOT NULL ORDER BY s.company")
	List<String> findDistinctCompanies();
}

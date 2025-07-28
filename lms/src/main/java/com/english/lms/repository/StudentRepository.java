package com.english.lms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.english.lms.entity.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {

    // studentId로 학생을 조회
    Optional<StudentEntity> findByStudentId(String studentId);

    // studentId 존재 여부 확인
    boolean existsByStudentId(String studentId);

    // 중복 없이 나이 목록 조회
    @Query("SELECT DISTINCT s.age FROM StudentEntity s WHERE s.age IS NOT NULL ORDER BY s.age")
    List<String> findDistinctAges();

    // 중복 없이 영어 레벨 목록 조회
    @Query("SELECT DISTINCT s.englishLevel FROM StudentEntity s WHERE s.englishLevel IS NOT NULL ORDER BY s.englishLevel")
    List<Integer> findDistinctEnglishLevels();

    // 중복 없이 회사 목록 조회
    @Query("SELECT DISTINCT s.company FROM StudentEntity s WHERE s.company IS NOT NULL ORDER BY s.company")
    List<String> findDistinctCompanies();
}

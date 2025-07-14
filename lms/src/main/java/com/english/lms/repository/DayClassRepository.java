package com.english.lms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.DayClassEntity;

public interface DayClassRepository extends JpaRepository<DayClassEntity, Integer> {
    // 기존 기능 (클래스 번호로 찾기)
    List<DayClassEntity> findByClassNum(Integer classNum);

    // 추가 기능: studentNum, classDate로 찾기
    List<DayClassEntity> findByStudentNumAndClassDate(Integer studentNum, LocalDate classDate);
}

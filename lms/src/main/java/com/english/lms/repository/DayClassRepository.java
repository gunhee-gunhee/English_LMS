package com.english.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.DayClassEntity;

public interface DayClassRepository extends JpaRepository<DayClassEntity, Integer> {
    List<DayClassEntity> findByClassNum(Integer classNum);
}
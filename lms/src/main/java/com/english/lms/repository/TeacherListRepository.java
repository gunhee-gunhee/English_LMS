package com.english.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.english.lms.entity.TeacherEntity;

public interface TeacherListRepository extends JpaRepository<TeacherEntity, Integer>, TeacherRepositoryCustom {

}

package com.english.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.english.lms.entity.ClassApplyEntity;

public interface InfoRepository extends Repository<ClassEntity, Integer> {
    @Query("SELECT new com.english.lms.info.TextInfo(t.name) " +
           "FROM ClassEntity c JOIN TextEntity t ON c.textNum = t.textNum " +
           "WHERE c.studentNum = :studentNum")
    List<TextInfo> findTextInfosByStudentNum(@Param("studentNum") Integer studentNum);
}

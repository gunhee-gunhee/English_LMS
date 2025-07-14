package com.english.lms.repository;

import com.english.lms.entity.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InfoRepository extends JpaRepository<TextEntity, Integer> {

    // studentNum(학생번호)로 연결된 모든 교재명(name) 조회
    @Query("SELECT t.name FROM TextEntity t WHERE t.textNum IN (" +
           "SELECT c.textNum FROM ClassEntity c WHERE c.studentNum = :studentNum)")
    List<String> findTextNamesByStudentNum(@Param("studentNum") Integer studentNum);

    // 필요시 교재 전체 리스트 등 기본 메서드는 JpaRepository가 제공
}

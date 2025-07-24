package com.english.lms.repository;

import com.english.lms.entity.DayOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DayOffRepository extends JpaRepository<DayOffEntity, Integer> {
    // 특정 기간의 공휴일
    List<DayOffEntity> findByOffDateBetween(java.sql.Date start, java.sql.Date end);

    // 단일 날짜 공휴일
    DayOffEntity findByOffDate(java.sql.Date date);

    // 연도별 전체 휴일 (관리자 화면에서 사용)
    @Query("SELECT d FROM DayOffEntity d WHERE YEAR(d.offDate) = :year ORDER BY d.offDate")
    List<DayOffEntity> findByYear(@Param("year") int year);

    @Query("SELECT d FROM DayOffEntity d WHERE YEAR(d.offDate) = :year")
    List<DayOffEntity> findAllByYear(@Param("year") int year);
}

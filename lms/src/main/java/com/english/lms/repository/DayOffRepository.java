package com.english.lms.repository;

import java.util.List;
import com.english.lms.entity.DayOffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOffRepository extends JpaRepository<DayOffEntity, Integer> {
    // 특정 달의 모든 공휴일 조회
    List<DayOffEntity> findByOffDateBetween(java.sql.Date start, java.sql.Date end);

    // 단일 날짜 공휴일 조회
    DayOffEntity findByOffDate(java.sql.Date date);
}

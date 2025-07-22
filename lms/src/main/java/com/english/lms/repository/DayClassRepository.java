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

    // ★ 추가: 해당 날짜 전체 수업 조회 (강사 예약 체크용)
    List<DayClassEntity> findByClassDate(LocalDate classDate);
}

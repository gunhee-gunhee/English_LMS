package com.english.lms.repository;

import com.english.lms.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Integer> {

    // 결석 취소 처리 때 absent_date와 student_num으로 row를 찾음
    Optional<PointEntity> findByStudentNumAndAbsentDate(Integer studentNum, LocalDate absentDate);

    // ★ 학생별, 타입별 포인트 row 목록 (예: 무료/보강포인트 등)
    List<PointEntity> findByStudentNumAndType(Integer studentNum, String type);

    // ★ 조건에 맞는 결석포인트 row만 조회
    List<PointEntity> findByStudentNumAndTypeAndPointAmountGreaterThanEqualAndExpiresAtGreaterThanEqualOrderByExpiresAtAsc(
            Integer studentNum,
            String type,
            Integer pointAmount,
            LocalDate expiresAt
    );
}

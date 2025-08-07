package com.english.lms.repository;

import com.english.lms.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    
    List<PointEntity> findByStudentNumAndTypeAndPointAmountGreaterThanEqual(
            Integer studentNum,
            String type,
            Integer pointAmount
    );


    @Query(
    	    value = """
    	        SELECT point_num
    	        FROM lms_point
    	        WHERE student_num = :studentNum
    	          AND type = :classType
    	          AND point_amount > 0
    	          AND (expires_at IS NULL OR expires_at > NOW())
    	        ORDER BY created_at DESC
    	        LIMIT 1
    	        """,
    	    nativeQuery = true
    	)
	Integer findPointNumForDelete(@Param("studentNum") Integer studentNum, @Param("classType") String classType);


    
    

}

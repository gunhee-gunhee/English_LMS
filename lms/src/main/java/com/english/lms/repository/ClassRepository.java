package com.english.lms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.english.lms.entity.ClassEntity;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer>, ClassRepositoryCustom {

    List<ClassEntity> findByStudentNum(Integer studentNum);

    @Query("SELECT c.teacherNum FROM ClassEntity c WHERE c.studentNum = :studentNum")
    Integer findTeacherNumByStudentNum(@Param("studentNum") Integer studentNum);

    @Query(value = "SELECT * FROM lms_class WHERE student_num = :studentNum LIMIT 1", nativeQuery = true)
    ClassEntity findByStudentNumQuery(@Param("studentNum") Integer studentNum);

    // ★ 년도 목록 (class_month에서 년도만 추출, 중복 없이)
    @Query(value = "SELECT DISTINCT SUBSTRING(class_month, 1, 4) AS year FROM lms_class ORDER BY year", nativeQuery = true)
    List<String> findDistinctYearsFromClassMonth();
}

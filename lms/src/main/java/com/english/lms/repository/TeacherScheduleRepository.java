package com.english.lms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.english.lms.entity.TeacherScheduleEntity;

public interface TeacherScheduleRepository extends JpaRepository<TeacherScheduleEntity, Integer> {

    // 기존 기능: 특정 강사의 모든 스케줄 삭제
    @Modifying
    @Query("DELETE FROM TeacherScheduleEntity t WHERE t.teacherNum = :teacherNum")
    void deleteByTeacherNum(@Param("teacherNum") Integer teacherNum);

    // ★ 추가: 요일별(is_available=1) 가능한 slot 조회 (일본어 요일)
    @Query("SELECT t FROM TeacherScheduleEntity t WHERE t.weekDay = :weekDay AND t.isAvailable = 1")
    List<TeacherScheduleEntity> findByWeekDayAndIsAvailable(@Param("weekDay") String weekDay);
}

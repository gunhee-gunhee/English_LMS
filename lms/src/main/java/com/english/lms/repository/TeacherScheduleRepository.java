package com.english.lms.repository;

import java.time.LocalDate;
import java.time.LocalTime;
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


	// 指定された日付とスロットに既に授業がある講師番号を取得
	// （例：2025年8月12日 10:00〜10:20の時間帯に授業が入っている講師を検索）
	@Query(value =  """
			SELECT teacher_num
			FROM lms_day_class
			WHERE class_date = :date
			  AND start_time < :endSlot
			  AND end_time < :startSlot
			""", nativeQuery = true)
	List<Integer> findBusyTeacher(
			@Param("date") LocalDate date, 
			@Param("startSlot") LocalTime slot, 
			@Param("endSlot") LocalTime plusMinutes);

	
	
	// 曜日・時間の条件に一致する講師番号のリストを取得
	@Query(value = """
			SELECT teacher_num
			FROM lms_teacher_schedule
			WHERE week_day IN (:weekDays)
			  AND time_slot IN (:slots)
			  AND is_available = 1
		 GROUP BY teacher_num
		 Having COUNT(*) = :slotCount
			""", nativeQuery = true)
	List<Integer> findDayTimeTeacher(
			@Param("weekDays") List<String> weekDays, 
			@Param("slots") List<LocalTime> slots, 
			@Param("slotCount") int slotCount);
	
	
	// is_available = 1 の講師のデータをすべて取得 
	@Query(value = """
			SELECT t.teacher_num, t.id, t.nickname, z.zoom_id
			FROM lms_teacher t
			JOIN lms_teacher_schedule ts ON t.teacher_num = ts.teacher_num
			INNER JOIN lms_zoom_account z ON t.zoom_num = z.zoom_num
			WHERE ts.is_available = 1
			  AND t.nullity = 0
			  AND t.teacher_num IN (:dayTimeTeacherNums)
			GROUP BY t.teacher_num, t.id, t.nickname, z.zoom_id
			""", nativeQuery = true)
	List<Object[]> findAllAvailableTeacher( @Param("dayTimeTeacherNums") List<Integer> dayTimeTeacherNums);

	//定期授業を登録する時、該当授業のis_availableを'0'にする。
	@Modifying
	@Query(value = """
			UPDATE lms_teacher_schedule
			SET is_available = 0
			WHERE teacher_num = :teacherNum
			  AND week_day = :weekDay
			  AND time_slot = :slot
			""", nativeQuery=true)
	void updateAvailable(@Param("teacherNum") Integer teacherNum, @Param("weekDay") String weekDay, @Param("slot")LocalTime slot);


}

package com.english.lms.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.english.lms.dto.TeacherDTO;

public interface TeacherScheduleService {

	// 指定された月と曜日に該当する日付をリストとして抽出
	List<LocalDate> getAvailableDates(int year, int month, List<DayOfWeek> days, LocalDate today);
	
	// 授業可能な講師リストを抽出
	List<TeacherDTO> findAvailableTeachers(List<LocalDate> dates, List<String> weekDays, LocalTime startTime, LocalTime end, String teacherSort, String teacherDir);
	
	
	
}

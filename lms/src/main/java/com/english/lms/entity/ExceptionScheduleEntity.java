package com.english.lms.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lms_teacher_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionScheduleEntity {

	//exceptionSchedule番号
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exception_schedule_num")  
	private Integer exceptionScheduleNum;
	
	//講師番号
	@Column(name = "teacher_num", nullable = false)
	private Integer teacherNum;
	
	//授業日
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	//タイムスロット
	@Column(name = "time_slot", nullable = false)
	private LocalTime timeSlot;
	
	//使用可能な時間か
	@Column(name = "is_available", nullable = false)
	private Boolean isAvailable;
	
	
}

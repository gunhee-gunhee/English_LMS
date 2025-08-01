package com.english.lms.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSlotDTO {

	private Integer teacherNum;
	private LocalDate date;
	private LocalTime timeSlot;
}

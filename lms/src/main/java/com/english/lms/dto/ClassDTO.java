package com.english.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDTO {

	private Integer classNum;
	private Integer studentNum;
	private Integer teacherNum;
	private String weekDays;
	private Integer textNum;
}

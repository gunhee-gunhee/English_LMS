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
	private String classType;
	
	//授業時間
    private Integer startHour;
    private Integer startMinute;
    
    private Integer endHour;
    private Integer endMinute;
    
    
    private String studentId;
    private String textName;
    
    
    
    
}

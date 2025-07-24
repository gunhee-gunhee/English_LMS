package com.english.lms.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.english.lms.dto.ClassDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoomMeetingServiceImpl implements ZoomMeetingService {@Override
	
	public void makeleClass(ClassDTO classDTO) {
//	System.out.println("studentNum " + classDTO.getStudentNum());
//	System.out.println("weekDays " +classDTO.getWeekDays());
//	System.out.println("startHour " + classDTO.getStartHour());
//	System.out.println("startMinute "+ classDTO.getStartMinute());
//	System.out.println("endHour " + classDTO.getEndHour());
//	System.out.println("endMinute " +classDTO.getEndMinute());
//	System.out.println("textNum " +classDTO.getTextNum());
//	System.out.println("teacherNum " +classDTO.getTeacherNum());
	
	//start time 
	ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
	//시간을 수업시작시간과 맞춰야함. 
	String startTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
	
	//end_date_time
	String endDateTime = "2099-12-31T23:59:59";
	
	//duration 
	int startHour = classDTO.getStartHour();
	int startMinute = classDTO.getStartMinute();
	int endHour = classDTO.getEndHour();
	int endMinute = classDTO.getEndMinute();
	
	// 開始時間・終了時間をそれぞれ分単位（Hour × 60 + Minute）に変換する
	int startTotal = startHour * 60 + startMinute;
	int endTotal = endHour * 60 + endMinute;
	
	//duration 出す。
	int duration = endTotal - startTotal;
	
	
	
	
	//type
	//repeat_interval
	//weekly_days
	//monthly_week_day
	// host_video
	//participant_video
	//join_before_host
	//must_upon_entry
	//wating_room = false
	//approval_type = 0 (참가승인방식 0 : 자동)
	//registration_type
	
		
	}

}

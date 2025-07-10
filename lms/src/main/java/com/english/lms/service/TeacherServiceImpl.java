package com.english.lms.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.english.lms.dto.TeacherDTO;
import com.english.lms.dto.TeacherScheduleDTO;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.entity.TeacherScheduleEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.TeacherScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
	
	private final TeacherRepository teacherRepository;
	private final TeacherScheduleRepository teacherScheduleRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public boolean existsById(String id) {
		// Idの重複チェック
		return  teacherRepository.findByTeacherId(id).isPresent();
	}

	@Override
	@Transactional // 講師とスケジュールの登録が両方成功したら保存完了
	public void registerTeacher(TeacherDTO teacherDTO) {
		
		//基本情報を保存する。
		  // 1. 기본 정보 저장
        TeacherEntity teacher = TeacherEntity.builder()
                .teacherId(teacherDTO.getId())
                .password(passwordEncoder.encode(teacherDTO.getPassword()))
                .nickname(teacherDTO.getNickname())
                .joinDate(LocalDateTime.now())
                .nullity(false)
                .role(Role.TEACHER)
                .build();
        
        //登録
        TeacherEntity entity = teacherRepository.save(teacher);
		Integer teacherNum = entity.getTeacherNum();
		
		//scheduleh保存
		for(TeacherScheduleDTO schedule : teacherDTO.getSchedules()) {
			
		
		// 時間（時と分）を結合してLocalTimeを作成
		//LocalTime.of（hour, minute) 
		LocalTime startTime = LocalTime.of(schedule.getStartHour(),schedule.getStartMinute());
		LocalTime endTime = LocalTime.of(schedule.getEndHour(), schedule.getEndMinute());
		
		// 曜日ごとにタイムスロットを保存
		for(String weekday : schedule.getWeekdays()) {
			
			//時間初期化
			LocalTime time = startTime;
			
			while (!time.isAfter(endTime)) {
				TeacherScheduleEntity scheduleEntity = TeacherScheduleEntity.builder()
						.teacherNum(teacherNum)
						.weekDay(weekday)
						.timeSlot(time)
						.isAvailable(1)
						.build();
				
				teacherScheduleRepository.save(scheduleEntity);
				
				time = time.plusMinutes(10); 
			}
		}
		
		}
		//weekday : String -> int
//		private int weekDayToInt(String weekday) {
//			case "日" -> 0;
//	        case "月" -> 1;
//	        case "火" -> 2;
//	        case "水" -> 3;
//	        case "木" -> 4;
//	        case "金" -> 5;
//	        case "土" -> 6;
//		}
	}

}

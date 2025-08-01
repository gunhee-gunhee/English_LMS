package com.english.lms.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.english.lms.dto.TeacherDTO;
import com.english.lms.repository.TeacherScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherScheduleServiceImpl implements TeacherScheduleService {

	private final TeacherScheduleRepository teacherScheduleRepository;

	// スロット生成用ユーティリティ（10分単位）
	private List<LocalTime> getTimeSlots(LocalTime startTime, LocalTime end) {

		List<LocalTime> slots = new ArrayList<>();

		LocalTime t = startTime;
		while (t.isBefore(end)) {
			slots.add(t);
			t = t.plusMinutes(10);
		}

		return slots;
	}

	@Override
	public List<LocalDate> getAvailableDates(int year, int month, List<DayOfWeek> days, LocalDate startDate) {

		List<LocalDate> dates = new ArrayList<>();

		// 月の初日と最終日
		LocalDate firstDay = LocalDate.of(year, month, 1);
		LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

		// 本日より前の日付は除外
		LocalDate startDay = startDate.isAfter(firstDay) ? startDate : firstDay;

		// 日付の曜日が指定された授業曜日（days）に含まれていれば、授業日リストに追加
		LocalDate cursor = startDay;
		while (!cursor.isAfter(lastDay)) {
			if (days.contains(cursor.getDayOfWeek())) {
				dates.add(cursor);
				
			}
			cursor = cursor.plusDays(1);
		}
System.out.println("수업가능한 일자 리스트 : " + dates);
		return dates;
	}
	
	

	// 授業可能な講師リストを抽出
	@Override
	public List<TeacherDTO> findAvailableTeachers(
			List<LocalDate> dates, 
			List<String> weekDays,
			LocalTime startTime, 
			LocalTime endTime,
			String teacherSort,
			String teacherDir
			) {
		// 10分単位のスロットに分割（例：10:00、10:10）
		List<LocalTime> slots = getTimeSlots(startTime, endTime);
System.out.println("time slot : " + slots);
		
		// 指定された日付とスロットに既に授業がある講師番号を取得
		Set<Integer> busyTeacherNums = new HashSet<>();
		for (LocalDate date : dates) {
			for (LocalTime slot : slots) {
				List<Integer> busyTeacher = teacherScheduleRepository.findBusyTeacher(date, slot, slot.plusMinutes(10));
				busyTeacherNums.addAll(busyTeacher);
			}
		}
		
		// 曜日・時間の条件に一致する講師番号のリストを取得
		int slotCount =  weekDays.size() * slots.size();
System.out.println("slotCount" + slotCount);
		List<Integer> dayTimeTeacherNums = teacherScheduleRepository.findDayTimeTeacher( 
				weekDays,        // ["月", "水", "金"] 
			    slots,        // [10:00, 10:10, 10:20] 
			    slotCount
			);
System.out.println("dayTimeTeacherNums : " + dayTimeTeacherNums );

		// is_available = 1 , nullity = 0 の講師を全件取得
		List<Object[]> rows = teacherScheduleRepository.findAllAvailableTeacher(dayTimeTeacherNums);
		List<TeacherDTO> availableTeachers = new ArrayList<>();
		for (Object[] row : rows) {
			TeacherDTO dto = TeacherDTO.builder()
					.teacherNum((Integer) row[0])
					.id((String) row[1])
					.nickname((String) row[2])
					.zoomId((String) row[3])
					.build();

			// 該当時間に授業がない講師だけをリストに追加
			if (!busyTeacherNums.contains(dto.getTeacherNum())) {
				availableTeachers.add(dto);
			}

		}
		
		// 許可されたカラムだけソート対象にする（ホワイトリスト）
		List<String> SORT_COLUMNS = Arrays.asList(
				"id", "nickname", "zoomId"
				);
		// 指定されたカラムが許可リストにない場合、nicknameでソートする
		if(!SORT_COLUMNS.contains(teacherSort)) {
			teacherSort = "nickname";
		}
		
		//Sort
		Comparator<TeacherDTO> comparator = switch (teacherSort) {
		case "id" -> Comparator.comparing(
				TeacherDTO::getId, Comparator.nullsFirst(String::compareTo)
				);
		case "nickname" -> Comparator.comparing(
				TeacherDTO::getNickname, Comparator.nullsFirst(String::compareTo)
				);
		case "zoomId" -> Comparator.comparing(
				TeacherDTO::getZoomId, Comparator.nullsFirst(String::compareTo)
				);
		default -> Comparator.comparing(
				TeacherDTO::getNickname, Comparator.nullsFirst(String::compareTo)
				);
		};
		
		if("desc".equalsIgnoreCase(teacherDir)) {
			comparator = comparator.reversed();
		}
		
		availableTeachers.sort(comparator);
		
		
		// 授業可能な講師リスト
		return availableTeachers;
	}

}

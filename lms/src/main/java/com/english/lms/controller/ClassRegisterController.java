package com.english.lms.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.english.lms.advice.TeacherAdvice;
import com.english.lms.dto.ClassDTO;
import com.english.lms.dto.StudentDTO;
import com.english.lms.dto.TeacherDTO;
import com.english.lms.entity.TextEntity;
import com.english.lms.service.AdminStudentService;
import com.english.lms.service.TeacherScheduleService;
import com.english.lms.service.TeacherService;
import com.english.lms.service.TextService;
import com.english.lms.service.ZoomMeetingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ClassRegisterController {

    private final TeacherAdvice teacherAdvice;

	private final AdminStudentService adminStudentService;
	private final TeacherService teacherService;
	private final TextService textService;
	private final ZoomMeetingService zoomMeetingService;
	private final TeacherScheduleService teacherScheduleService;

//    ClassRegisterController(TeacherAdvice teacherAdvice) {
//        this.teacherAdvice = teacherAdvice;
//    }

	// Page移動
	@GetMapping("/admin/class/register")
	public String showPage(
			// 学生リスト
			@RequestParam(name = "studentSort", defaultValue = "studentId") String studentSort,
			@RequestParam(name = "studentDir", defaultValue = "asc") String studentDir,
			
			Model model
			) {

		// ソート条件と並び順は Pageable で構成
		// dir に渡された値が大文字・小文字を区別せず "desc" と一致すれば true！
		Sort.Direction studentDirection = studentDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC
				: Sort.Direction.ASC;

		List<StudentDTO> studentList = adminStudentService.getActiveStudents(studentSort, studentDir);

		List<TextEntity> texts = textService.findAll();
		model.addAttribute("studentList", studentList);
		model.addAttribute("studentSort", studentSort);
		model.addAttribute("studentDir", studentDir);
		model.addAttribute("classDTO", new ClassDTO());
		model.addAttribute("texts", texts);

		return "admin/class-register";
	}

	/**
	 * class-register : 講師リストを表示
	 * 
	 */
	@GetMapping("/admin/available-teacher-list")
	@ResponseBody
	public List<TeacherDTO> getAvailableTeachers(
			@RequestParam(name = "weekDays") List<String> weekDays, 
			@RequestParam(name = "startTime") String startTime,
			@RequestParam(name = "endTime") String endTime,
			@RequestParam(name = "startDate") LocalDate startDate,
			@RequestParam(name = "teacherSort", defaultValue = "teacherId") String teacherSort,
			@RequestParam(name = "teacherDir", defaultValue = "asc") String teacherDir
			) {

System.out.println("선택한 요일 : " + weekDays);
		
		
		
		// 漢字の曜日 → DayOfWeek 列挙型に変換
		List<DayOfWeek> days = weekDays.stream().map(weekDay -> {
			return switch (weekDay) {
			case "日" -> DayOfWeek.SUNDAY;
			case "月" -> DayOfWeek.MONDAY;
			case "火" -> DayOfWeek.TUESDAY;
			case "水" -> DayOfWeek.WEDNESDAY;
			case "木" -> DayOfWeek.THURSDAY;
			case "金" -> DayOfWeek.FRIDAY;
			case "土" -> DayOfWeek.SATURDAY;
			default -> throw new IllegalArgumentException("不正な曜日が指定されました。" + weekDay);
			};
		}).toList();
		
System.out.println("변환된 요일값 : " + days);
			
		// 指定された条件から日付Listを作成
		int year = startDate.getYear();
		int month = startDate.getMonthValue();

		List<LocalDate> availableDates = teacherScheduleService.getAvailableDates(
				year, month, days, startDate
				);

		// 利用可能な講師リストを取得
		List<TeacherDTO> teachers = teacherScheduleService.findAvailableTeachers(
				availableDates,
				weekDays,
				LocalTime.parse(startTime), 
				LocalTime.parse(endTime),
				teacherSort,
				teacherDir
			);
       return teachers;
	}

	/**
	 * 定期授業を登録するメソッド
	 * 
	 * @param classDTO
	 * @return
	 */

	@PostMapping("/admin/regular/register")
	public String registerRegularClass(@ModelAttribute("classDTO") ClassDTO classDTO) {

		// ZoomMeetingService
		zoomMeetingService.makeleClass(classDTO);

		return "redirect:/admin/class/register";
	}

}
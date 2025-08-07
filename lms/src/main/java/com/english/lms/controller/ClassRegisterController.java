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
			//定期授業のパラメータ
			@RequestParam(name = "weekDays", required = false) List<String> weekDays, 
			@RequestParam(name = "startTime", required = false) String startTime,
			@RequestParam(name = "endTime", required = false) String endTime,
			@RequestParam(name = "startDate", required = false) LocalDate startDate,
			
			//非定期授業のパラメータ
			@RequestParam(name = "date", required = false) String classDate,
			@RequestParam(name = "time", required = false) String classTime,
			
			//Sort, Dir
			@RequestParam(name = "teacherSort", defaultValue = "teacherId") String teacherSort,
			@RequestParam(name = "teacherDir", defaultValue = "asc") String teacherDir
			) {

		//授業が可能な日
		List<LocalDate> availableDates;
		//授業曜日
	    List<String> weekDaysToUse;
	    //授業が始まる時間
	    LocalTime start;
	    //授業終了時間
	    LocalTime end;
	    
	    
		//非定期授業
	    if(classDate != null && classTime != null) {
	    	//選択した日
	    	LocalDate date = LocalDate.parse(classDate);
	    	//選択した時間
	    	String[] timeRange = classTime.split("~");
	    	
	    	start = LocalTime.parse(timeRange[0].trim());
	    	end = LocalTime.parse(timeRange[1].trim());
	    	availableDates = List.of(date); //classDate -> LocalDate 変換した　授業日
	    	
	    	//DayOfWeek 列挙型　→　漢字の曜日　に変換
	    	String weekDayKanji = switch (date.getDayOfWeek()) {
            case SUNDAY -> "日";
            case MONDAY -> "月";
            case TUESDAY -> "火";
            case WEDNESDAY -> "水";
            case THURSDAY -> "木";
            case FRIDAY -> "金";
            case SATURDAY -> "土";
        };
        weekDaysToUse = List.of(weekDayKanji);
        
        System.out.println("非定期授業: 曜日=" + date + ", 授業時間=" + classTime);
        System.out.println("授業曜日: " + weekDayKanji);
	    
	    }else {
	    	System.out.println("DEBUG | weekDays: " + weekDays);
	    	System.out.println("DEBUG | startTime: " + startTime);
	    	System.out.println("DEBUG | endTime: " + endTime);
	    	System.out.println("DEBUG | startDate: " + startDate);

	    	
	    	//定義授業
	    	
	    	 if (weekDays == null || startTime == null || endTime == null || startDate == null) {
	             throw new IllegalArgumentException("定期授業に必要な情報がすべて入力されていません");
	         }
	    	 
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
			 
			availableDates = teacherScheduleService.getAvailableDates(
						year, month, days, startDate
						);
			 start = LocalTime.parse(startTime);
		       end = LocalTime.parse(endTime);
		       weekDaysToUse = weekDays;
		       
		       System.out.println("定期授業: 曜日=" + weekDays + ", 授業開始=" + startTime + ", 授業終了=" + endTime);
	    }
		

		// 利用可能な講師リストを取得
		List<TeacherDTO> teachers = teacherScheduleService.findAvailableTeachers(
				availableDates,
				weekDaysToUse,
				start, 
				end,
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
		zoomMeetingService.makeleRegisterClass(classDTO);

		return "redirect:/admin/class/register";
	}
	

	/**
	 * 非定期授業を登録するメソッド
	 * 
	 * @param classDTO
	 * @return
	 */
	@PostMapping("/admin/irregular/register")
	public String registerIrregularClass(@ModelAttribute("classDTO") ClassDTO classDTO) {
		
		// ZoomMeetingService
		zoomMeetingService.makeleIrregularClass(classDTO);
		
		return "redirect:/admin/class/register";
	}

}
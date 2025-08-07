package com.english.lms.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.english.lms.advice.TeacherAdvice;
import com.english.lms.dto.ClassDTO;
import com.english.lms.entity.ClassEntity;
import com.english.lms.entity.DayClassEntity;
import com.english.lms.repository.ClassRepository;
import com.english.lms.repository.DayClassRepository;
import com.english.lms.repository.StudentRepository;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.TeacherScheduleRepository;
import com.english.lms.repository.ZoomAccountRepository;
import com.english.lms.util.TimeSlotUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoomMeetingServiceImpl implements ZoomMeetingService {

	private final WeekDayConverterService weekDayConverterService;
	private final ZoomTokenService zoomTokenService;
	private final TeacherRepository teacherRepository;
	private final ZoomAccountRepository zoomAccountRepository;
	private final StudentRepository studentRepository;

	private final ClassRepository classRepository;
	private final DayClassRepository dayClassRepository;

	private final TeacherScheduleService teacherScheduleService;
	private final TeacherScheduleRepository teacherScheduleRepository;

	@Override
	@Transactional
	public void makeleClass(ClassDTO classDTO) {
//	System.out.println("studentNum " + classDTO.getStudentNum());
//	System.out.println("weekDays " +classDTO.getWeekDays());
//	System.out.println("startHour " + classDTO.getStartHour());
//	System.out.println("startMinute "+ classDTO.getStartMinute());
//	System.out.println("endHour " + classDTO.getEndHour());
//	System.out.println("endMinute " +classDTO.getEndMinute());
//	System.out.println("textNum " +classDTO.getTextNum());
//	System.out.println("teacherNum " +classDTO.getTeacherNum());

		int startHour = classDTO.getStartHour();
		int startMinute = classDTO.getStartMinute();
		int endHour = classDTO.getEndHour();
		int endMinute = classDTO.getEndMinute();

		/**
		 * start time : formatted / end_date_time :endDateTime
		 */

		LocalDate startDate = classDTO.getStartDate();
		System.out.println("startDate : " + startDate);
		LocalTime time = LocalTime.of(startHour, startMinute);

		// タイムゾーンを明確に指定
		ZonedDateTime zonedStart = ZonedDateTime.of(startDate, time, ZoneId.of("Asia/Tokyo"));

		// フォーマット
		String formatted = zonedStart.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

//確認（後で消す。）
		System.out.println("start time : " + formatted);

		// end_date_time
		// 授業終了日が決まった時、更新する。
		String endDateTime = "2099-12-31T23:59:59";

		/**
		 * duration
		 */
		// 開始時間・終了時間をそれぞれ分単位（Hour × 60 + Minute）に変換する
		int startTotal = startHour * 60 + startMinute;
		int endTotal = endHour * 60 + endMinute;

		// duration 出す。
		int duration = endTotal - startTotal;

		/**
		 * weekly_days :weeklyDays
		 */
		String weekDays = classDTO.getWeekDays();
		// 曜日 → 数字 変換
		String weeklyDays = weekDayConverterService.toZoomWeeklyDays(weekDays);

		/**
		 * Zoom API 用のリクエストボディを作成（Map 形式で記述）
		 */

		// recurrence : Zoom の繰り返しミーティング用の詳細設定（recurrence の各項目）
		Map<String, Object> recurrence = new HashMap<>();

		recurrence.put("type", 2); // 繰り返しタイプ : 2=Weekly
		recurrence.put("repeat_interval", 1); // 1週間ごとに繰り返し
		recurrence.put("weekly_days", weeklyDays); // 繰り返しの曜日
		recurrence.put("end_date_time", endDateTime); // 繰り返し終了タイム（ISO + TZ）

		// setting: 詳細設定オプション（settings オブジェクト）
		Map<String, Object> settings = new HashMap<>();

		settings.put("host_video", true); // 授業開始時にホストのカメラを自動ON/OFF - ON
		settings.put("participant_video", true); // 授業開始時に参加者のカメラを自動ON/OFF - ON
		settings.put("join_before_host", true); // ホストより先に参加者の入室を許可
		settings.put("waiting_room", false); // 待機室を使用する
		settings.put("approval_type", 2); // 自動承認

		// requestBody :Zoom API のミーティング作成リクエストに使用される全パラメータの Map
		Map<String, Object> requestBody = new HashMap<>();

		// topic : 講義室の名前（studentNickname/授業時間/授業の曜日/授業の種類

		// studentNicName
		int studentNum = classDTO.getStudentNum();
		String studentNickName = studentRepository.findBystudentNum(studentNum);

		// classStartTime : 授業開始時間 （ex.10:00)
		LocalTime classStartTime = LocalTime.of(startHour, startMinute); // 10, 0 → 10:00
		// classEndTime : 授業終了時間（ex. 10:20)
		LocalTime classEndTime = LocalTime.of(endHour, endMinute); // 10, 20 → 10:20
		// 授業時間
		String timeRange = classStartTime + "~" + classEndTime;

		// classType
		String classType = "定期";

		String className = studentNickName + "/" + timeRange + "/" + weeklyDays + "/" + classType;
		System.out.println("className : " + className);
		requestBody.put("type", 8); // 定期ミーティング（Recurring Meeting）
		requestBody.put("topic", className); // class name
		requestBody.put("start_time", formatted); // 初めての授業が開始する時間
		requestBody.put("duration", duration); // 授業が進む時間(20分~）
		requestBody.put("timezone", "Asia/Tokyo"); // TimeZone
		requestBody.put("recurrence", recurrence); // Zoom ミーティングの繰り返し設定
		requestBody.put("settings", settings); // Zoom ミーティングの詳細設定

		/**
		 * HTTP POST リクエストを送信する
		 */

		// HTTP header(AccessToken, cotent-Type)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// アクセストークンを取得する
		String accessToken = zoomTokenService.getAccessToken();
		headers.setBearerAuth(accessToken); // AccessToken

		// HttpEntity(body+header)
		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

		// RestTemplate を使って POST リクエストを送信
		RestTemplate restTemplate = new RestTemplate();

		// zoomNum
		Integer teacherNum = classDTO.getTeacherNum(); // 나중에 teacherNum으로 변경
		Integer zoomNum = teacherRepository.findZoomIdByTeacherNum(teacherNum);
		if (zoomNum == null)
			throw new IllegalArgumentException("講師に紐づく Zoom アカウントが存在しません");

		// zoomId
		String zoomId = zoomAccountRepository.findZoomIdByZoomNum(zoomNum);
		if (zoomId == null)
			throw new IllegalArgumentException("Zoom アカウントが存在しません.");

		String url = "https://api.zoom.us/v2/users/" + zoomId + "/meetings";

		ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);

		// responseBody = API から受け取った文字列 (JSON)
		String responseBody = response.getBody();

		// Zoom API のレスポンス（String responseBody）から join_url を抽出
		// ObjectMapper：Jackson の JSON パーサーオブジェクトを生成
		ObjectMapper objectMapper = new ObjectMapper();

		String joinUrl = null;
		String meetingId = null;
		String teacherUrl = null;

		try {
			// responseBody（String 型）に含まれる JSON 文字列をパースして、
			// root（JsonNode 型）という「ツリー構造の JSON オブジェクト」を作成する
			JsonNode root = objectMapper.readTree(responseBody);

			// root から "join_url" というキーの値を取り出し、String 型で取得する
			// join_url：参加者用リンク
			joinUrl = root.path("join_url").asText();
			// meetingId：会議室の固有ID
			meetingId = root.path("id").asText();
			// start_url：ホスト(講師）専用の入場リンク
			teacherUrl = root.path("start_url").asText();

			System.out.println("회의실 입장 링크: " + joinUrl);
			System.out.println("회의실 ID(meetingId): " + meetingId);
			System.out.println("호스트 입장 링크(start_url): " + teacherUrl);

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		/**
		 * lms_class table 保存
		 * 
		 * student_num, teacher_num, week_days, text_num, start_date,
		 * start_time(:classStartTime), end_time(:classEndTime), class_month
		 * 
		 */

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

		// classMonth
		String classMonth = startDate.format(formatter);
		System.out.println("classMonth : " + classMonth);

		ClassEntity classEntity = ClassEntity.builder().studentNum(classDTO.getStudentNum())
				.teacherNum(classDTO.getTeacherNum()).weekDays(classDTO.getWeekDays()).textNum(classDTO.getTextNum())
				.startDate(classDTO.getStartDate()).startTime(classStartTime).endTime(classEndTime)
				.classMonth(classMonth).build();

		// save
		ClassEntity savedClassEntity = classRepository.save(classEntity);

		/**
		 * lms_day_class table 保存
		 * 
		 * class_num, class_type(:classType), class_name, class_date,
		 * start_time(:classStartTime), end_time(:classEndTime), zoom_link(:joinUrl),
		 * teacher_link(:teacherUrl),
		 * 
		 * zoom_meeting_id(:meetingId), student_num, teacher_num
		 * 
		 */

		// classNum
		int classNum = savedClassEntity.getClassNum();

		// class_type(:classType)
		// class_name(:className)

		// class_date
		int year = startDate.getYear();
		int month = startDate.getMonthValue();

		// String weekDays -> List<String> weekDayList
		List<String> weekDayList = Arrays.stream(weekDays.split(",")).map(String::trim).collect(Collectors.toList());

		// 漢字の曜日 → DayOfWeek 列挙型に変換
		List<DayOfWeek> days = weekDayList.stream().map(weekDay -> {
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

		List<LocalDate> classDate = teacherScheduleService.getAvailableDates(year, month, days, startDate);
		System.out.println("classDate : " + classDate);

		for (LocalDate date : classDate) {

			DayClassEntity dayClassEntity = DayClassEntity.builder().classNum(classNum).classType(classType)
					.className(className).classDate(date).startTime(classStartTime).endTime(classEndTime)
					.zoomLink(joinUrl).teacherLink(teacherUrl).zoomMeetingId(meetingId).studentNum(studentNum)
					.teacherNum(teacherNum).attendance(0).build();

			dayClassRepository.save(dayClassEntity);

		}
		
		//定期授業を登録する時、該当授業のis_availableを'0'にする。
		List<LocalTime> timeSlots = TimeSlotUtils.getTimeSlots(classStartTime, classEndTime);
		
		for(String weekDay : weekDayList) {
			for(LocalTime slot : timeSlots) {
				teacherScheduleRepository.updateAvailable(
						teacherNum,
						weekDay,
						slot
						);
			}
		}
	}

}

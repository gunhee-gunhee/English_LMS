package com.english.lms.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.english.lms.dto.ClassDTO;
import com.english.lms.repository.TeacherRepository;
import com.english.lms.repository.ZoomAccountRepository;
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
	
	@Override
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
	 * start time : formatted 
	 * end_date_time :endDateTime
	 */
	
	LocalDate today = LocalDate.now(ZoneId.of("Asia/Tokyo"));
	LocalTime time = LocalTime.of(startHour, startMinute);
	
	//タイムゾーンを明確に指定
	ZonedDateTime zonedStart = ZonedDateTime.of(today, time, ZoneId.of("Asia/Tokyo"));
	
	//フォーマット
	String formatted = zonedStart.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	
//確認（後で消す。）
	System.out.println("start time : " + formatted);
	
	//end_date_time
	//授業終了日が決まった時、更新する。
	String endDateTime = "2099-12-31T23:59:59";
	
	/**
	 * duration
	 * */	
	// 開始時間・終了時間をそれぞれ分単位（Hour × 60 + Minute）に変換する
	int startTotal = startHour * 60 + startMinute;
	int endTotal = endHour * 60 + endMinute;
	
	//duration 出す。
	int duration = endTotal - startTotal;
	
	/**
	 * weekly_days :weeklyDays
	 * */
	String weekDays = classDTO.getWeekDays();
	//曜日　→　数字　変換
	String weeklyDays = weekDayConverterService.toZoomWeeklyDays(weekDays);
	
	
	/**
	 *  Zoom API 用のリクエストボディを作成（Map 形式で記述）
	 * */
	
	//recurrence : Zoom の繰り返しミーティング用の詳細設定（recurrence の各項目）
	Map<String, Object> recurrence = new HashMap<>();
	
	recurrence.put("type", 2); 						//繰り返しタイプ : 2=Weekly
	recurrence.put("repeat_interval", 1); 			//1週間ごとに繰り返し
	recurrence.put("weekly_days", weeklyDays); 		//繰り返しの曜日
	recurrence.put("end_date_time", endDateTime);	// 繰り返し終了タイム（ISO + TZ）
	
	
	//setting: 詳細設定オプション（settings オブジェクト）
	Map<String, Object> settings = new HashMap<>();
	
	settings.put("host_video", true); 				//授業開始時にホストのカメラを自動ON/OFF - ON
	settings.put("participant_video", true); 		//授業開始時に参加者のカメラを自動ON/OFF - ON
	settings.put("join_before_host", true);  		// ホストより先に参加者の入室を許可
	settings.put("waiting_room", false);     		//待機室を使用する
	settings.put("approval_type", 2);    			//自動承認
	
	
	//requestBody :Zoom API のミーティング作成リクエストに使用される全パラメータの Map
	Map<String, Object> requestBody = new HashMap<>();
	
	requestBody.put("type", 8); 					//定期ミーティング（Recurring Meeting）
	requestBody.put("topic", "TEMP NAME"); 			//class name
	requestBody.put("start_time", formatted); 		//初めての授業が開始する時間
	requestBody.put("duration", duration);			//授業が進む時間(20分~）
	requestBody.put("timezone", "Asia/Tokyo");  	//TimeZone
	requestBody.put("recurrence", recurrence);		//Zoom ミーティングの繰り返し設定
	requestBody.put("settings", settings); 			//Zoom ミーティングの詳細設定
	
	
	/**
	 * HTTP POST リクエストを送信する
	 */
	
	// HTTP header(AccessToken, cotent-Type) 
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	
	// アクセストークンを取得する
	String accessToken = zoomTokenService.getAccessToken();
	headers.setBearerAuth(accessToken); //AccessToken
	
	
	// HttpEntity(body+header)
	HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(requestBody, headers);
	
	// RestTemplate を使って POST リクエストを送信
	RestTemplate restTemplate = new RestTemplate();
	
	//zoomNum
	Integer teacherNum = classDTO.getTeacherNum();  //나중에 teacherNum으로 변경
	Integer zoomNum= teacherRepository.findZoomIdByTeacherNum(teacherNum);
	if(zoomNum == null) throw new IllegalArgumentException("講師に紐づく Zoom アカウントが存在しません");
	
	//zoomId
	String zoomId = zoomAccountRepository.findZoomIdByZoomNum(zoomNum);
	if(zoomId == null) throw new IllegalArgumentException("Zoom アカウントが存在しません.");
	
	String url = "https://api.zoom.us/v2/users/" + zoomId + "/meetings";
	
	ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
	
	
	// responseBody = API から受け取った文字列 (JSON)
	String responseBody = response.getBody();
	
	// Zoom API のレスポンス（String responseBody）から join_url を抽出
	// ObjectMapper：Jackson の JSON パーサーオブジェクトを生成
	ObjectMapper objectMapper = new ObjectMapper();
	
	try {
		// responseBody（String 型）に含まれる JSON 文字列をパースして、
		// root（JsonNode 型）という「ツリー構造の JSON オブジェクト」を作成する
		JsonNode root =  objectMapper.readTree(responseBody);
		
		// root から "join_url" というキーの値を取り出し、String 型で取得する
		// join_url：講義室への参加リンク
		String joinUrl = root.path("join_url").asText();
		System.out.println("회의실 입장 링크: " + joinUrl);
		
	} catch (JsonMappingException e) {
		e.printStackTrace();
	} catch (JsonProcessingException e) {
		e.printStackTrace();
	}
	
	
	
	
	
	}

}

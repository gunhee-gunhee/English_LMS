package com.english.lms.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * 曜日（漢字）→ 数字 に変換
 */

@Service
public class WeekDayConverterService {
	// 曜日を Zoom の数字形式（1=日, 2=月, ..., 7=土）にマッピングする Map
	private static final Map<String, String> DAY_MAP = Map.of(
			"日","1", 
			"月","2", 
			"火","3", 
			"水","4", 
			"木","5", 
			"金","6", 
			"土","7"
			);
	
	/**
	 * // 曜日文字列を Zoom API の形式（数値の曜日文字列）に変換するメソッド
	 * // 例："月,水,金" → "2,4,6"
	 * @param weekDays 曜日文字列（例："月,水,金"）
	 * @return Zoom API が要求する数字形式の曜日（例："2,4,6"）をカンマ区切りで返却
	 * */
	public String toZoomWeeklyDays(String weekDays) {
		// 入力が空または null の場合は、空文字を返す
		if(weekDays == null || weekDays.isBlank()) return "";
		
		return Arrays.stream(weekDays.split(","))// カンマで分割
				.map(String::trim) //各曜日の前後の空白を削除
				.map(DAY_MAP::get) //get() : 曜日(key) → 数字(value)
				.filter(Objects::nonNull) // null を除外するためのフィルター
				.collect(Collectors.joining(",")); // カンマで再結合する
		
				
	}
	
}

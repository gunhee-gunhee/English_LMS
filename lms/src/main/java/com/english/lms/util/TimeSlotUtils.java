package com.english.lms.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotUtils {

	// スロット生成用ユーティリティ（10分単位）
		public static List<LocalTime> getTimeSlots(LocalTime startTime, LocalTime end) {

			List<LocalTime> slots = new ArrayList<>();

			LocalTime t = startTime;
			while (t.isBefore(end)) {
				slots.add(t);
				t = t.plusMinutes(10);
			}

			return slots;
		}

}

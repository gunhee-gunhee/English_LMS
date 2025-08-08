package com.english.lms.service;

import java.util.List;
import java.util.Map;

import com.english.lms.dto.ClassDTO;

public interface ZoomMeetingService {

	//定期授業の登録
	void makeleRegisterClass(ClassDTO classDTO);

	//非定期授業の登録
	void makeleIrregularClass(ClassDTO classDTO);
	
	//Zoomアカウントに対応するzoomLinkを照会
//	Map<String, List<Map<String, Object>>> getMeetingsByZoomAccount();

}

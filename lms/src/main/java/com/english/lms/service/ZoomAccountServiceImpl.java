package com.english.lms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.english.lms.entity.ZoomAccountEntity;
import com.english.lms.repository.ZoomAccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoomAccountServiceImpl implements ZoomAccountService {
	
	private final ZoomAccountRepository zoomAccountRepository;
	@Override
	public List<String> getAllZoomIds() {
		List<ZoomAccountEntity> list = zoomAccountRepository.findByLinkedFalse();
		
		List<String> result = new ArrayList<>();
		for(ZoomAccountEntity z : list) {
			result.add(z.getZoomId());
		}
		
		
		return result;
	}

}

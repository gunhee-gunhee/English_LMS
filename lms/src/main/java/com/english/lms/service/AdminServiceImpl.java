package com.english.lms.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.english.lms.dto.AdminDTO;
import com.english.lms.entity.AdminEntity;
import com.english.lms.enums.Role;
import com.english.lms.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void registerCenter(AdminDTO dto) {
		// Center会員登録機能
		
		AdminEntity center = new AdminEntity();
		
		center.setAdminId(dto.getId());
		center.setPassword(passwordEncoder.encode(dto.getPassword()));
		center.setRole(Role.CENTER);
		
		adminRepository.save(center);
	}

	@Override
	public boolean existsById(String id) {
		// AdminRepositoryで　centerIdとadminIdの重複チェック
		return adminRepository.findByAdminId(id).isPresent();
	}

	@Override
	public void registerAdmin(AdminDTO dto) {
		// Admin会員登録機能
		
		AdminEntity admin = new AdminEntity();
		
		admin.setAdminId(dto.getId());
		admin.setPassword(passwordEncoder.encode(dto.getPassword()));
		admin.setRole(Role.ADMIN);
		
		adminRepository.save(admin);
		
	}


}

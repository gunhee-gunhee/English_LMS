package com.english.lms.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.english.lms.dto.CustomUserDetails;
import com.english.lms.entity.AdminEntity;
import com.english.lms.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CenterUserDetailsService implements UserDetailsService {
	
	
	private final AdminRepository adminRepository;
	@Override
	public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {
		System.out.println("입력받은 아이디 : " +  adminId);
		
		AdminEntity center = adminRepository.findByAdminId(adminId)
				.orElseThrow(() -> {
					
					return new UsernameNotFoundException("アカウントなし");
				});
			
				System.out.println("dB에서 가져온 객체 : " + center);
		
		return new CustomUserDetails(
				center.getAdminId(),
				center.getPassword(),
				center.getRole()
				);
	}

}

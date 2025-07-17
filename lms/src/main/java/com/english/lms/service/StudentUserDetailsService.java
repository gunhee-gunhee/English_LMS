package com.english.lms.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.english.lms.dto.CustomUserDetails;
import com.english.lms.entity.StudentEntity;
import com.english.lms.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentUserDetailsService implements UserDetailsService {
	
	private final StudentRepository studentRepository;
	
	@Override
	public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {
		System.out.println("입력받은 아이디 : " + studentId);
		
		StudentEntity student = studentRepository.findByStudentId(studentId)
				
				.orElseThrow(() -> {
				
					return new UsernameNotFoundException("アカウントなし");
				});
		
		return new CustomUserDetails(
				student.getStudentId(),
				student.getPassword(),
				student.getRole()
				);
				
	
	}
	
	
}

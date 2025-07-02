package com.english.lms.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.english.lms.dto.CustomUserDetails;
import com.english.lms.entity.TeacherEntity;
import com.english.lms.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherUserDetailsService implements UserDetailsService{
	
	private final TeacherRepository teacherRepository;

	
	@Override
	public UserDetails loadUserByUsername(String teacherId) throws UsernameNotFoundException {
		System.out.println("입력받은 아이디 : " + teacherId);
		
		TeacherEntity teacher = teacherRepository.findByTeacherId(teacherId)
		.orElseThrow(() -> {
			
			return new UsernameNotFoundException("アカウントなし");
		});
		System.out.println("dB에서 가져온 객체 : " + teacher);
		
		return new CustomUserDetails(
				teacher.getTeacherId(),
				teacher.getPassword(),
				teacher.getRole()
				);
	}

}

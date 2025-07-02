package com.english.lms.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.english.lms.dto.CustomUserDetails;
import com.english.lms.enums.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//ログイン成功後の処理をカスタムで制御する
@Component
public class StudentLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		
		if(!user.getRole().equals(Role.STUDENT)) {
			response.sendRedirect("/admin/login?error=role");
			return;
		}
		
		response.sendRedirect("/student/mypage");
		
	}

	
}

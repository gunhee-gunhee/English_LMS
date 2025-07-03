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

@Component
public class CenterLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		
		if(!user.getRole().equals(Role.CENTER)) {
			response.sendRedirect("/center/login?error=role");
			return;
		}
		
		response.sendRedirect("/admin/regular-class-list");
		
	}

}

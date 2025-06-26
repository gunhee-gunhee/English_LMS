package com.english.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //시큐리티 설정파일 
public class SecurityConfig {

	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	        	//url에 따른 접근권한 설정 
	            .authorizeHttpRequests(auth -> auth
	            	//누구나	
	                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
	                //관리자만 
	                .requestMatchers("/admin/**").hasRole("ADMIN")
	                //그외모든 요청 . 로그인후 접근 
	                .anyRequest().authenticated()
	            )
	            
	            //로그인 관련설정 
	            .formLogin(form -> form
	                .loginPage("/login")  //커스텀한 로그인 페이지
	                .defaultSuccessUrl("/") //성공시 이동할 기본 경로 
	                .permitAll() //누구나 접근 가능
	            )
	            
	            //로그아웃 관련 설정 
	            .logout(logout -> logout
	            		//로그아웃 성공 시 이동할 경로 
	                .logoutSuccessUrl("/login?logout")
	                .permitAll() //누구나 접근 가능 
	            );

	        return http.build(); //SecurityFilterChain 객체 생성
	    }
}

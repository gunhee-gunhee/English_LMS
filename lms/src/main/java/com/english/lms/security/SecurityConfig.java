package com.english.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.english.lms.service.AdminUserDetailsService;
import com.english.lms.service.StudentUserDetailsService;
import com.english.lms.service.TeacherUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration //Security 設定クラス
@EnableWebSecurity //Spring Security 活性化
@RequiredArgsConstructor // finalフィールドはコンストラクタを通じて自動的に依存性注入されます
public class SecurityConfig {
	
	//フィールド
	private final AdminUserDetailsService adminService;
	private final AdminLoginSuccessHandler adminSuccessHandler;
	private final AdminLoginFailureHandler adminFailureHandler;
	
	private final StudentUserDetailsService studentService;
	private final StudentLoginSuccessHandler studentSuccessHandler;
	private final StudentLoginFailureHandler studentFailureHandler;
	
	private final TeacherUserDetailsService teacherService;
	private final TeacherLoginSuccessHandler teacherSuccessHandler;
	private final TeacherLoginFailureHandler teacherFailureHandler;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	/*
	 * @Bean(name = "adminSecurity")
	 * 
	 * @Order(1) SecurityFilterChain adminSecurity(HttpSecurity http) throws
	 * Exception {
	 * 
	 * http //URLごとのアクセス制御を設定 .securityMatcher("/admin/**") // 管理者 URLのみ適用
	 * .authorizeHttpRequests(auth -> auth .requestMatchers("/admin/login",
	 * "/admin/css/**", "/admin/js/**").permitAll() .anyRequest().hasRole("ADMIN") )
	 * .formLogin(form -> form .loginPage("/admin/login") // カスタムログインページ
	 * .loginProcessingUrl("/admin/login") // ログイン処理用URL（POST）
	 * .successHandler(adminSuccessHandler) // ログイン成功時のハンドラー
	 * .failureHandler(adminFailureHandler) // ログイン失敗時のハンドラー ) .logout(logout ->
	 * logout .logoutUrl("/admin/logout") // ログアウト用URL
	 * .logoutSuccessUrl("/admin/login?logout") // ログアウト成功後の遷移先 )
	 * .userDetailsService(adminService); // 管理者アカウントの取得サービス
	 * 
	 * return http.build(); }
	 * 
	 * @Bean(name = "studentSecurity")
	 * 
	 * @Order(2) SecurityFilterChain studentSecurity(HttpSecurity http) throws
	 * Exception {
	 * 
	 * http .securityMatcher("/student/**") .authorizeHttpRequests(auth -> auth
	 * .requestMatchers( "/student/login", "/student/css/**", "/student/js/**",
	 * "/student/apply", // ✅ GET/POST "/student/thanks" // ✅ GET ).permitAll()
	 * .anyRequest().hasRole("STUDENT") ) .formLogin(form -> form
	 * .loginPage("/student/login") .loginProcessingUrl("/student/login")
	 * .successHandler(studentSuccessHandler) .failureHandler(studentFailureHandler)
	 * ) .logout(logout -> logout .logoutUrl("/student/logout")
	 * .logoutSuccessUrl("/student/login?logout") )
	 * .userDetailsService(studentService);
	 * 
	 * return http.build(); }
	 * 
	 * @Bean(name = "teacherSecurity")
	 * 
	 * @Order(3) SecurityFilterChain teacherSecurity(HttpSecurity http) throws
	 * Exception{
	 * 
	 * http .securityMatcher("/teacher/**") .authorizeHttpRequests(auth -> auth
	 * .requestMatchers("/teacher/login").permitAll()
	 * .anyRequest().hasRole("TEACHER") ) .formLogin(form -> form
	 * .loginPage("/teacher/login") .loginProcessingUrl("/teacher/login")
	 * .successHandler(teacherSuccessHandler) .failureHandler(teacherFailureHandler)
	 * ) .userDetailsService(teacherService);
	 * 
	 * 
	 * return http.build(); }
	 */
	
	@Bean(name = "permitAllSecurity")
	@Order(99) // 가장 마지막 순서로 등록되도록
	public SecurityFilterChain permitAllSecurity(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .anyRequest().permitAll() // 모든 요청 허용
	        )
	        .csrf().disable(); // (선택) CSRF도 비활성화

	    return http.build();
	}

	

}

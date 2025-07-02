package com.english.lms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.english.lms.service.AdminUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration //Security 設定クラス
@EnableWebSecurity //Spring Security 活性化
@RequiredArgsConstructor // finalフィールドはコンストラクタを通じて自動的に依存性注入されます
public class SecurityConfig {
	
	//フィールド
	private final AdminUserDetailsService adminService;
	private final CustomAuthenticationSuccessHandler successHandler;
	private final CustomAuthenticationFailureHandler failureHandler;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
	
	@Bean(name = "adminSecurity")
	SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {
		
		http
			//URLごとのアクセス制御を設定
		 	.securityMatcher("/admin/**") // 管理者　URLのみ適用
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/admin/login", "/admin/css/**", "/admin/js/**").permitAll()
	            .anyRequest().hasRole("ADMIN")
	            )
	        .formLogin(form -> form
	        		.loginPage("/admin/login") // カスタムログインページ
	        		.loginProcessingUrl("/admin/login") // ログイン処理用URL（POST）
	        		.successHandler(successHandler)  // ログイン成功時のハンドラー
	        		.failureHandler(failureHandler)  // ログイン失敗時のハンドラー
	        		)
	        .logout(logout -> logout
	        		.logoutUrl("/admin/logout")     // ログアウト用URL
	        		.logoutSuccessUrl("/admin/login?logout") // ログアウト成功後の遷移先
	        		)
	        .userDetailsService(adminService);  // 管理者アカウントの取得サービス
	        
		return http.build();
	}
	

}

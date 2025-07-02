package com.english.lms.service;

import java.util.Optional;

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
public class AdminUserDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;

	// ログインフォームへのID・パスワード入力後
	// loadUserByUsername()を呼び出す
	@Override
	public UserDetails loadUserByUsername(String adminId) throws UsernameNotFoundException {

		// DBからユーザー情報を取得する
		AdminEntity admin = adminRepository.findByAdminId(adminId)
			// 指定されたIDの管理者が存在しない場合、UsernameNotFoundExceptionをスローします。
			.orElseThrow(() -> {

				return new UsernameNotFoundException("アカウントなし");
			});

		// Spring Securityに渡すと、それがAuthenticationの中に格納される
		return new CustomUserDetails(
			admin.getAdminId(),
			admin.getPassword(),
			admin.getRole()
		);
	}
}


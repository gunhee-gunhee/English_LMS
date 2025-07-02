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
		System.out.println("ログイン試行中のID: " + adminId); //ユーザーが入力したID 

		// DBからユーザー情報を取得する
		AdminEntity admin = adminRepository.findByAdminId(adminId)
			// 指定されたIDの管理者が存在しない場合、UsernameNotFoundExceptionをスローします。
			.orElseThrow(() -> {
				System.out.println("指定されたIDの管理者が存在しません！");
				return new UsernameNotFoundException("アカウントなし");
			});

		System.out.println("管理者情報取得成功 → ID: " + admin.getAdminId() + ", ROLE: " + admin.getRole()); //DBで持ってきたアカウント情報

		// Spring Securityに渡すと、それがAuthenticationの中に格納される
		return new CustomUserDetails(
			admin.getAdminId(),
			admin.getPassword(),
			admin.getRole()
		);
	}
}


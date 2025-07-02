package com.english.lms.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.english.lms.dto.CustomUserDetails;
import com.english.lms.enums.Role;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
//ログイン成功後の処理をカスタムで制御する
public class AdminLoginSuccessHandler implements AuthenticationSuccessHandler {
    
	// ログイン成功時に自動的に実行されるメソッド
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, //要求オブジェクト
                                        HttpServletResponse response,//応答オブジェクト
                                        //ログインしたユーザーの認証情報(CustomUserDetails:dbで呼び出したアカウント情報)
                                        Authentication authentication) throws IOException {
		
		// getPrincipal()は、ログイン中のユーザーオブジェクトを返す
		// ロールやIDなどの情報を直接チェックできる
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        
        // ユーザーのロールがADMINでない場合は、認証失敗として処理し、再度ログインページへリダイレクト

        if (!user.getRole().equals(Role.ADMIN)) {
            response.sendRedirect("/admin/login?error=role");
            return;
        }
        
     // ロールがADMINの場合は、/admin/regular-class-listにリダイレクト
        response.sendRedirect("/admin/regular-class-list");
    }
}
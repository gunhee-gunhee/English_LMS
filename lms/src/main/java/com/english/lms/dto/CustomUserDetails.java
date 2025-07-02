package com.english.lms.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.english.lms.enums.Role;

public class CustomUserDetails implements UserDetails {
	
	//このクラスの固有ID
	private static final long serialVersionUID = 1L;
	
	//フィールド
    private final String id;
    private final String password;
    private final Role role;
    
    public CustomUserDetails(String id, String password, Role role) {
    	this.id = id;
    	this.password = password;
    	this.role =role;
    }
    
    public Role getRole() { 
    	return role;
    }
	
    //Roleをsecurityに伝達する。
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// Spring Securityが認識できる文字列に
//		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
//	}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        System.out.println("🎭 CustomUserDetails.getAuthorities(): " + authorities);
        return authorities;
    }

	@Override
	public String getPassword() {
		//パスワードを変換
		System.out.println("🔐 CustomUserDetails.getPassword(): " + password);
		return password;
		
	}

	@Override
	public String getUsername() {
		// idを変換
		return id;
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true; //	満了
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true; 
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;//アカウントの状態
	}

	

}

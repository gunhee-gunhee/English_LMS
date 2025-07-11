package com.english.lms.entity;

import com.english.lms.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//admin_num INT NOT NULL AUTO_INCREMENT PRIMARY KEY,  -- 
//id VARCHAR(40) NOT NULL,                            -- 
//password VARCHAR(20) NOT NULL,                      -- 
//role VARCHAR(255)                                   -- 
@Entity
@Table(name = "lms_admin")
@Getter
@Setter
@NoArgsConstructor //デフォルトコンストラクタを生成します。フィールドに final 修飾子がある場合は、(access = AccessLevel.PROTECTED) を付け加えてください。
public class AdminEntity {
	
	//管理者番号
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_num")
	private Integer adminNum;
	
	//管理者ID
	@Column(name="id", nullable = false, length = 40)
	private String adminId;
	
	//パスワード
	@Column(name = "password", nullable = false, length = 20)
	private String password;
	
	//権限
	@Enumerated(EnumType.STRING) //enumを文字列に保存する。
	@Column(name="role" , nullable = false)
	private Role role;

	
	
	

}

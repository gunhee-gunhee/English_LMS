package com.english.lms.entity;

import java.time.LocalDateTime;

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

@Entity
@Table(name = "lms_teacher")
@Getter
@Setter
@NoArgsConstructor
public class TeacherEntity {
	
	//講師番号
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "teacher_num")
	private Integer teacherNum;
	
	//講師ID
	@Column(name="id", nullable = false, length = 40)
	private String teacherId;
	
	//パスワード
	@Column(name = "password", nullable = false, length = 255)
	private String password;
	
	//ニックネーム
	@Column(name = "nickname", nullable = true, length = 20)
	private String nickname;
	
	//登録日
	@Column(name = "join_date", nullable = false)
	private LocalDateTime joinDate;
	
	//利用状態
	@Column(name = "enable", nullable = true)
	private  Boolean enable;
	
	//権限
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;
	
	
}

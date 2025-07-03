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
@Table(name = "lms_student")
@Getter
@Setter
@NoArgsConstructor //기본 생성자 생성 - 만약 필드에 fianl이 있을 경우 (access = AccessLevel.PROTECTED)추가 
public class StudentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_num")
	private Integer studentNum;
	
	@Column(name="id", nullable = false, length = 40)
	private String studentId;
	
	@Column(name="password", nullable = false, length = 20)
	private String password;
	
	@Column(name="nickname", length = 20)
	private String nickname;

    @Column(name = "nickname_jp", length = 20)
    private String nicknameJp;

    @Column(name = "age", length = 10)
    private String age;

    @Column(name = "english_level")
    private Integer englishLevel;

    @Column(name = "english_purpose", length = 255)
    private String englishPurpose;

    @Column(name = "signup_path", length = 255)
    private String signupPath;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    @Column(name = "enable", nullable = false)
    private Integer enable;

    @Column(name = "point", nullable = false)
    private Integer point;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
	
	
}

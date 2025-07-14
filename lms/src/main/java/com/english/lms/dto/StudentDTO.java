package com.english.lms.dto;

import com.english.lms.enums.Role;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Integer studentNum;
    private String id;
    private String password;
    private String passwordCheck;      // PasswordCheck → passwordCheck
    private String nickname;
    private String nicknameJp;
    private String age;
    private Integer englishLevel;      // English_level → englishLevel
    private String englishPurpose;     // English_purpose → englishPurpose
    private String signupPath;         // Signup_path → signupPath
    private String joinDate;
    private Boolean nullity;
    private Integer point;
    private String role;
    private String company;

    
    private String teacherNickname;
}

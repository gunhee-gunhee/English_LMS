package com.english.lms.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
    private String id;
    private String password;
    private String passwordCheck; 
    private String nickname;
    private String nicknameJp;
    private String company;
    private String age;
    private Integer englishLevel;
    private String englishPurpose;
    private String signupPath;
    private Integer nullity;
}

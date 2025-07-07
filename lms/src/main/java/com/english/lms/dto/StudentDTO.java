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
    private String nickname_jp;
    private String age;
    private Integer english_level;
    private String english_purpose;
    private String signup_path;
    private Integer enable;
}

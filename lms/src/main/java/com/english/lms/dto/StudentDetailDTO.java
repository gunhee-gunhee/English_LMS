package com.english.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StudentDetailDTO {
    private int studentNum;
    private String nickname;
    private String nicknameJp;
    private String age;
    private Integer englishLevel;
    private String englishPurpose;
}
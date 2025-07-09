package com.english.lms.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    private String id;
    private String password;
    private String passwordCheck; // 폼에서 입력 받음

    
 
}

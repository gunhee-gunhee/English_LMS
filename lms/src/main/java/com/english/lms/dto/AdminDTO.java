package com.english.lms.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
	private Integer adminNum;
    private String id;
    private String password;
    private String passwordCheck; 
    private String role;

    
 
}

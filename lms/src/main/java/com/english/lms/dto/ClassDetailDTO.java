package com.english.lms.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ClassDetailDTO {
    private int dayClassNum;
    private Date classDate;      
    private String weekDay;        
    private String studentName;
    private String studentNameJp;
    private String classType;
    private String textNum;        
    private String comment;
    private String progress;
    private String name;
}

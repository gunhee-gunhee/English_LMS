package com.english.lms.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassApplyDto {

    private String studentId;           

    private LocalDate startDate;

    private String course;               // 曜日: tue-thu, mon-wed-fri 

    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;
}

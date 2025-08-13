package com.english.lms.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassApplyDto {

    private Long applyNum;              // 신청 번호
    private String studentNickname;     // 학생 닉네임
    private String studentId;           // 학생 아이디

    private LocalDate startDate;        // 시작일
    private String course;              // 코스 (tue-thu, mon-wed-fri)

    private String firstChoice;         // 1희망
    private String secondChoice;        // 2희망
    private String thirdChoice;         // 3희망

    private LocalDateTime applyDate;    // 제출일
}

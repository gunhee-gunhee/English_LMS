package com.english.lms.dto;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrregularAvailableTimeDTO {
    private String timeLabel;           // "10:00~10:20"
    private List<Integer> teacherNums;  // 해당 시간에 가능한 강사 번호 리스트
}

package com.english.lms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrregularApplyRequestDTO {
    private Integer studentNum;      // 신청 학생 번호
    private String date;             // yyyy-MM-dd
    private String timeLabel;        // "13:20~13:40"
    private String type;             // "additional" (보강), "trial" (무료체험) 등
}

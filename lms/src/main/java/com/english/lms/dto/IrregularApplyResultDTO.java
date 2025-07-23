package com.english.lms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrregularApplyResultDTO {
    private boolean success;
    private String message;
    private String reservedTime;
    private String teacherNickname;
}

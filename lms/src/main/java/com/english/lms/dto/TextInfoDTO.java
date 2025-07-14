package com.english.lms.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@Builder
public class TextInfoDTO {
    private String name;

    // 생성자, getter
    public TextInfoDTO(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
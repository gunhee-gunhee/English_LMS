package com.english.lms.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeacherDto {
    private Integer teacherNum;
    private String id;
    private String password;
    private String nickname;
    private java.sql.Timestamp joinDate;
    private Boolean nullity;
    private String role;
}

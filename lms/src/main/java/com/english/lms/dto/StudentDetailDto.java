package com.english.lms.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class StudentDetailDto {

    private Integer studentNum;
    private String id;
    private String password;
    private String nickname;
    private String nicknameJp;
    private String age;
    private Integer englishLevel;
    private String englishPurpose;
    private String signupPath;
    private String joinDate;
    private Integer enable;
    private Integer point;
    private String role;
    private String company;

    //受講情報(複数受講可能)
    private List<ClassInfo> classes;

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ClassInfo {
        private Integer classNum;
        private Integer teacherNum;
        private List<String> weekDays; // ["月", "水", "金"]
        private List<DayClassInfo> schedules; 
    }

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DayClassInfo {
        private Integer dayClassNum;
        private String classType;
        private String className;
        private String classDate;
        private String startTime;
        private String endTime;
        
    }
}

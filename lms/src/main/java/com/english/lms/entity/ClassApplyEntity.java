package com.english.lms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "lms_class_apply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassApplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classApplyNum;

    private String studentId;

    private LocalDate startDate;

    private String course;

    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;

    @Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp applyDate;
}
